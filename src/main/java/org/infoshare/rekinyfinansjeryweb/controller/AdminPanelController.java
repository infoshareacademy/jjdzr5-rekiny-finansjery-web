package org.infoshare.rekinyfinansjeryweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin-panel")
public class AdminPanelController {

    @GetMapping
    public String getAdminPanel() {
        return "admin_panel";
    }
}
