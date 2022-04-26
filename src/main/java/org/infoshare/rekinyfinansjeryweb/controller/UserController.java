package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService usersService;

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getUser(Model model) {
        model.addAttribute("user", usersService.getUser());
        return "user";
    }
}
