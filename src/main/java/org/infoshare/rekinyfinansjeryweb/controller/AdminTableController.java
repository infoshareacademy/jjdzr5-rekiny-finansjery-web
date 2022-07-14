package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateFormDTO;
import org.infoshare.rekinyfinansjeryweb.service.AdminTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class AdminTableController {

    @Autowired
    AdminTableService adminTableService;

    @PostMapping("/delete-table/{date}")
    public String deleteTable(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, RedirectAttributes attributes) {
        adminTableService.deleteTable(date);
        attributes.addFlashAttribute("successMessage", "delete.table.success");
        return "redirect:/tables";
    }

    @PostMapping("/add-currency/{date}")
    public String postNewCurrency(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @Valid @ModelAttribute ExchangeRateFormDTO newExchangeRate, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || adminTableService.exchangeRateExists(date, newExchangeRate.getCode())) {
            if (adminTableService.exchangeRateExists(date, newExchangeRate.getCode())) {
                result.reject("Currency not added", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "add.currency.error");
            attributes.addFlashAttribute("errorMessageForAddCurrency", result.getAllErrors());
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminTableService.addExchangeRate(newExchangeRate);
            attributes.addFlashAttribute("successMessage", "add.currency.success");
        }
        return "redirect:/table/" + date;
    }

    @PostMapping("/add-currency")
    public String postNewCurrencyWithDate(@Valid @ModelAttribute ExchangeRateFormDTO newExchangeRate, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || adminTableService.exchangeRateExists(newExchangeRate.getDate(), newExchangeRate.getCode())) {
            if (adminTableService.exchangeRateExists(newExchangeRate.getDate(), newExchangeRate.getCode())) {
                result.reject("Currency not added", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "add.currency.error");
            attributes.addFlashAttribute("errorMessageForAddCurrency", result.getAllErrors());
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminTableService.addExchangeRate(newExchangeRate);
            attributes.addFlashAttribute("successMessage", "add.currency.success");
        }
        return "redirect:/table/" + newExchangeRate.getDate();
    }

    @PostMapping("/delete-currency/{date}/{code}")
    public String deleteCurrency(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("code") String code, RedirectAttributes attributes) {
        adminTableService.deleteExchangeRate(date, code);
        attributes.addFlashAttribute("successMessage", "delete.currency.success");
        return adminTableService.tableExists(date) ? "redirect:/table/" + date : "redirect:/tables";
    }

    @PostMapping("/edit-currency/{date}/{code}")
    public String editCurrency(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("code") String code, @Valid @ModelAttribute ExchangeRateFormDTO newExchangeRate, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors() || (adminTableService.exchangeRateExists(date, newExchangeRate.getCode()) && !newExchangeRate.getCode().equals(code))) {
            if (adminTableService.exchangeRateExists(date, newExchangeRate.getCode())) {
                result.reject("Currency not edited", "validation.currency.exists");
            }
            attributes.addFlashAttribute("errorMessage", "edit.currency.error");
            attributes.addFlashAttribute("errorMessageForEditCurrency", result.getAllErrors());
            attributes.addFlashAttribute("currencyCode", code);
            attributes.addFlashAttribute("invalidExchangeRate", newExchangeRate);
        } else {
            adminTableService.editExchangeRate(date, code, newExchangeRate);
            attributes.addFlashAttribute("successMessage", "edit.currency.success");
        }
        return "redirect:/table/" + date;
    }
}