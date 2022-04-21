package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.service.AdminPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin-panel")
public class AdminPanelController {

    @Autowired
    private AdminPanelService adminPanelService;

    @GetMapping
    public String getAdminPanel(Model model) {
        model.addAttribute("dailyExchangeRates", adminPanelService.getDailyExchangeRates());
        return "admin_panel";
    }
}
