package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.form.TableSettings;
import org.infoshare.rekinyfinansjeryweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/admin")
@Secured({"ROLE_ADMIN"})
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/add-table")
    public RedirectView postNewTable(@ModelAttribute TableSettings settings) {
        adminService.addTable(settings);
        return new RedirectView("/tables");
    }

    @PostMapping("/delete-table/{no}")
    public RedirectView deleteTable(@PathVariable("no") String no) {
        adminService.deleteTable(no.replace('_', '/'));
        return new RedirectView("/tables");
    }

    @PostMapping("/edit-table/{no}")
    public RedirectView editTable(@PathVariable("no") String no, @ModelAttribute TableSettings settings) {
        adminService.editTable(no.replace('_', '/'), settings);
        return new RedirectView("/tables");
    }

    @PostMapping("/add-currency/{no}")
    public RedirectView postNewTable(@PathVariable("no") String no, @ModelAttribute ExchangeRate newExchangeRate) {
        adminService.addCurrency(no.replace('_', '/'), newExchangeRate);
        return new RedirectView("/tables");
    }

    @PostMapping("/delete-currency/{no}/{code}")
    public RedirectView deleteCurrency(@PathVariable("no") String no, @PathVariable("code") String code) {
        adminService.deleteCurrency(no.replace('_', '/'), code);
        return new RedirectView("/tables");
    }

    @PostMapping("/edit-currency/{no}/{code}")
    public RedirectView editCurrency(@PathVariable("no") String no, @PathVariable("code") String code, @ModelAttribute ExchangeRate newExchangeRate) {
        adminService.editCurrency(no.replace('_', '/'), code, newExchangeRate);
        return new RedirectView("/tables");
    }
}
