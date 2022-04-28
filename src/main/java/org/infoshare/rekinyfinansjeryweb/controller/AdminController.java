package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.form.TableSettings;
import org.infoshare.rekinyfinansjeryweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/admin")
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
}
