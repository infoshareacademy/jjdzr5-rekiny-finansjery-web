package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ComponentController {
    @Autowired
    UserService userService;

    @ModelAttribute
    public void addAttribute(Model model){
        model.addAttribute("user", userService.getUser());
    }


}
