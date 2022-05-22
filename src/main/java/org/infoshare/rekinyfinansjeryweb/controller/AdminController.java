package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.formData.ExchangeRateForm;
import org.infoshare.rekinyfinansjeryweb.formData.DailyTableForm;
import org.infoshare.rekinyfinansjeryweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/admin", "*/admin"})
@Secured({"ROLE_ADMIN"})
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/add-table")
    public String postNewTable(@Valid @ModelAttribute DailyTableForm dailyTable, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || adminService.tableExists(dailyTable.getNo())) {
            if (adminService.tableExists(dailyTable.getNo())) {
                result.reject("Table not added", "validation.table.exists");
            }
            attributes.addFlashAttribute("errorMessage", "add.table.error");
            attributes.addFlashAttribute("errorMessageForAddTable", result.getAllErrors());
            attributes.addFlashAttribute("invalidDailyTable", dailyTable);
            return "redirect:/tables";
        }
        adminService.addTable(dailyTable);
        attributes.addFlashAttribute("successMessage", "add.table.success");
        return "redirect:/table/" + dailyTable.getNo().replace('/', '_');
    }

    @PostMapping("/delete-table/{no}")
    public String deleteTable(@PathVariable("no") String no, RedirectAttributes attributes) {
        adminService.deleteTable(no.replace('_', '/'));
        attributes.addFlashAttribute("successMessage", "delete.table.success");
        return "redirect:/tables";
    }

    @PostMapping("/edit-table/{no}")
    public String editTable(@PathVariable("no") String no, @Valid @ModelAttribute DailyTableForm dailyTable, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || (adminService.tableExists(dailyTable.getNo()) && !dailyTable.getNo().equals(no.replace('_', '/')))) {
            if (adminService.tableExists(dailyTable.getNo())) {
                result.reject("Table not edited", "validation.table.exists");
            }
            attributes.addFlashAttribute("errorMessage", "edit.table.error");
            attributes.addFlashAttribute("errorMessageForEditTable", result.getAllErrors());
            attributes.addFlashAttribute("invalidDailyTable", dailyTable);
            return "redirect:/table/" + no;
        }
        adminService.editTable(no.replace('_', '/'), dailyTable);
        attributes.addFlashAttribute("successMessage", "edit.table.success");
        return "redirect:/table/" + dailyTable.getNo().replace('/', '_');
    }

    @PostMapping("/add-currency/{no}")
    public String postNewTable(@PathVariable("no") String no, @Valid @ModelAttribute ExchangeRateForm newExchangeRate, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || adminService.currencyExists(no.replace('_', '/'), newExchangeRate.getCode())) {
            if (adminService.currencyExists(no.replace('_', '/'), newExchangeRate.getCode())) {
                result.reject("Currency not added", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "add.currency.error");
            attributes.addFlashAttribute("errorMessageForAddCurrency", result.getAllErrors());
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminService.addCurrency(no.replace('_', '/'), newExchangeRate);
            attributes.addFlashAttribute("successMessage", "add.currency.success");
        }
        return "redirect:/table/" + no;
    }

    @PostMapping("/delete-currency/{no}/{code}")
    public String deleteCurrency(@PathVariable("no") String no, @PathVariable("code") String code, RedirectAttributes attributes) {
        adminService.deleteCurrency(no.replace('_', '/'), code);
        attributes.addFlashAttribute("successMessage", "delete.currency.success");
        return "redirect:/table/" + no;
    }

    @PostMapping("/edit-currency/{no}/{code}")
    public String editCurrency(@PathVariable("no") String no, @PathVariable("code") String code, @Valid @ModelAttribute ExchangeRateForm newExchangeRate, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || (adminService.currencyExists(no.replace('_', '/'), newExchangeRate.getCode()) && !newExchangeRate.getCode().equals(code))) {
            if (adminService.currencyExists(no.replace('_', '/'), newExchangeRate.getCode())) {
                result.reject("Currency not edited", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "edit.currency.error");
            attributes.addFlashAttribute("errorMessageForEditCurrency", result.getAllErrors());
            attributes.addFlashAttribute("currencyCode", code);
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminService.editCurrency(no.replace('_', '/'), code, newExchangeRate);
            attributes.addFlashAttribute("successMessage", "edit.currency.success");
        }
        return "redirect:/table/" + no;
    }
}