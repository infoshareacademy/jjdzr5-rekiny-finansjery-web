package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.dto.user.CreateUserFormDTO;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SignupController {

    @Autowired
    UserService usersService;

    @GetMapping("/signup")
    public String signupForm(Model model){
            model.addAttribute("user", new CreateUserFormDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") CreateUserFormDTO user, BindingResult result, Model model){
        if (result.hasErrors()){
            return "signup";
        } else if (usersService.emailExists(user.getEmail())) {
            result.rejectValue("email", "validation.email");
            return "signup";
        }
        if (usersService.addUser(user)) {
            model.addAttribute("successMessage", "msg.success.signup");
        } else {
            model.addAttribute("errorMessage", "msg.error.signup");
        }
        return "index";
    }
}
