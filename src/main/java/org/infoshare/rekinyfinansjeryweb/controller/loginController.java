package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.data.User;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {

    @Autowired
    UserService usersService;

    @GetMapping("/login")
    public String loginForm(Model model){
            model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model){
        if (user.getEmail().isBlank() || user.getPassword().isBlank()) {
            model.addAttribute("msg", "These fields cannot be empty");
            return "login";
        }
        if (usersService.loginUser(user.getEmail(), user.getPassword())) {
            model.addAttribute("user", usersService.getUser());
            return "index";
        }
        model.addAttribute("msg", "The email address or password is incorrect - please try again.");
        return "login";
    }

    @GetMapping("signout")
    public String signout(Model model){
        usersService.signoutUser();
        model.addAttribute("user", new User());
        return "index";
    }
}
