package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.data.User;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    UserService usersService;

    @GetMapping("/signup")
    public String signupForm(Model model){
            model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model){
        System.out.println(user.getName());
        System.out.println(user.getLastname());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
//        if (newUser.getEmail().isBlank() || newUser.getPassword().isBlank()) {
//            model.addAttribute("msg", "These fields cannot be empty");
//            return "signup";
//        }
//        if (usersService.loginUser(newUser.getEmail(), newUser.getPassword())) {
//            model.addAttribute("user", usersService.getUser());
//            return "index";
//        }
//        model.addAttribute("msg", "The email address or password is incorrect - please try again.");
        return "signup";
    }
}
