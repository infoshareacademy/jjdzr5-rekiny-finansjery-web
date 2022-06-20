package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.DailyTableFormDTO;
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
import java.time.LocalDate;

@Controller
@RequestMapping(value = {"/admin", "*/admin"})
@Secured({"ROLE_ADMIN"})
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/delete-table/{date}")
    public String deleteTable(@PathVariable("date") LocalDate date, RedirectAttributes attributes) {
        adminService.deleteTable(date);
        attributes.addFlashAttribute("successMessage", "delete.table.success");
        return "redirect:/tables";
    }

//    @PostMapping("/edit-table/{date}")
//    public String editTable(@PathVariable("date") LocalDate date, @Valid @ModelAttribute LocalDate newDate, BindingResult result, RedirectAttributes attributes) {
//        if (result.hasErrors() || (adminService.tableExists(newDate) && !newDate.equals(date))) {
//            if (adminService.tableExists(newDate)) {
//                result.reject("Table not edited", "validation.table.exists");
//            }
//            attributes.addFlashAttribute("errorMessage", "edit.table.error");
//            attributes.addFlashAttribute("errorMessageForEditTable", result.getAllErrors());
//            attributes.addFlashAttribute("invalidDailyTable", newDate);
//            return "redirect:/table/" + date;
//        }
//        adminService.editTable(date, newDate);
//        attributes.addFlashAttribute("successMessage", "edit.table.success");
//        return "redirect:/table/" + newDate;
//    }

    @PostMapping("/add-currency/{date}")
    public String postNewTable(@PathVariable("date") LocalDate date, @Valid @ModelAttribute ExchangeRateFormDTO newExchangeRate, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || adminService.exchangeRateExists(date, newExchangeRate.getCode())) {
            if (adminService.exchangeRateExists(date, newExchangeRate.getCode())) {
                result.reject("Currency not added", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "add.currency.error");
            attributes.addFlashAttribute("errorMessageForAddCurrency", result.getAllErrors());
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminService.addExchangeRate(newExchangeRate);
            attributes.addFlashAttribute("successMessage", "add.currency.success");
        }
        return "redirect:/table/" + date;
    }

    @PostMapping("/delete-currency/{date}/{code}")
    public String deleteCurrency(@PathVariable("date") LocalDate date, @PathVariable("code") String code, RedirectAttributes attributes) {
        adminService.deleteExchangeRate(date, code);
        attributes.addFlashAttribute("successMessage", "delete.currency.success");
        return "redirect:/table/" + date;
    }

    @PostMapping("/edit-currency/{date}/{code}")
    public String editCurrency(@PathVariable("date") LocalDate date, @PathVariable("code") String code, @Valid @ModelAttribute ExchangeRateFormDTO newExchangeRate, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors() || (adminService.exchangeRateExists(date, newExchangeRate.getCode()) && !newExchangeRate.getCode().equals(code))) {
            if (adminService.exchangeRateExists(date, newExchangeRate.getCode())) {
                result.reject("Currency not edited", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "edit.currency.error");
            attributes.addFlashAttribute("errorMessageForEditCurrency", result.getAllErrors());
            attributes.addFlashAttribute("currencyCode", code);
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminService.editExchangeRate(date, code, newExchangeRate);
            attributes.addFlashAttribute("successMessage", "edit.currency.success");
        }
        return "redirect:/table/" + date;
    }
}