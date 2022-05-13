package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.form.SaveOfFiltrationSettings;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService usersService;

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getUser(Model model) {
        model.addAttribute("user", usersService.getUser());
        return "user";
    }

    @GetMapping("/filtration_preferences")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String userFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, Model model) {
        return "user_filtration_preferences_list";
    }

    @PostMapping("/filtration_preferences/add")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addUserFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, @ModelAttribute SaveOfFiltrationSettings filtrationSettings, Model model) {
        System.out.println(filtrationSettings);
        return "user_filtration_preferences_list";
    }

    @GetMapping("/filtration_preferences/delete")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String deleteUserFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, Model model) {
        return "user_filtration_preferences_list";
    }
}
