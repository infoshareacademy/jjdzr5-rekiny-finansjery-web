package org.infoshare.rekinyfinansjeryweb.controller;

import org.infoshare.rekinyfinansjeryweb.dto.user.UserAdminPanelDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.UserDTO;
import org.infoshare.rekinyfinansjeryweb.service.AdminPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/admin", "*/admin"})
@Secured({"ROLE_ADMIN"})
public class AdminPanelController {

    @Autowired
    AdminPanelService adminPanelService;

    @GetMapping("/panel")
    public String getPanel(Model model) {
        model.addAttribute("userList", adminPanelService.getAllUsers());
        model.addAttribute("userAdminPanel", new UserAdminPanelDTO());
        return "panel";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@ModelAttribute UserDTO userDTO, RedirectAttributes attributes) {
        adminPanelService.deleteUser(userDTO);
        attributes.addFlashAttribute("successMessage", "delete.user.success");
        return "redirect:/admin/panel";
    }

    @PostMapping("/edit-user")
    public String editUser(@Valid @ModelAttribute UserAdminPanelDTO userAdminPanelDTO, BindingResult result, RedirectAttributes attributes) {
        if (adminPanelService.usersMailExists(userAdminPanelDTO.getEmail(), userAdminPanelDTO.getId())) {
            result.reject("User exists", "validation.user.exists");
        }
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", "edit.user.error");
            attributes.addFlashAttribute("errorMessageForEditUser", result.getAllErrors());
        } else {
            adminPanelService.editUser(userAdminPanelDTO);
            attributes.addFlashAttribute("successMessage", "edit.user.success");
        }
        return "redirect:/admin/panel";
    }
}
