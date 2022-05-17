package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
import org.infoshare.rekinyfinansjeryweb.formData.SaveOfFiltrationSettings;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


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
        model.addAttribute("listOfSavedFiltrationSettings", getListOfSavedFiltrationSettings(principal));
        return "user_filtration_preferences_list";
    }

    @GetMapping("/filtration_preferences/add")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String redirectToUserFiltersFromAdd(){
        return "redirect:/user/filtration_preferences";
    }

    @GetMapping("/filtration_preferences/delete")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String redirectToUserFiltersFromDelete(){
        return "redirect:/user/filtration_preferences";
    }


    @PostMapping("/filtration_preferences/add")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String addUserFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal,
                                               @ModelAttribute @Valid SaveOfFiltrationSettings filtrationSettings,
                                               BindingResult result, Model model, @Autowired UsedCurrenciesService usedCurrenciesService) {
        if(result.hasErrors()){
            model.addAttribute("possibleCurrencies",
                    usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), filtrationSettings.getCurrency()));
            return "user_filtration_preferences_add_form";
        }
        else if(principal.getUser().getSavedFiltrationSettings().containsKey(filtrationSettings.getPreferenceName())){
            model.addAttribute("errorMessage", "filters.error.name.used");
            model.addAttribute("possibleCurrencies",
                    usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), filtrationSettings.getCurrency()));
            return "user_filtration_preferences_add_form";
        }

        if(isFiltrationSettingEmpty(filtrationSettings)){
            model.addAttribute("errorMessage", "filters.error.empty");
        }
        else{
            principal.getUser().getSavedFiltrationSettings().put(filtrationSettings.getPreferenceName(), filtrationSettings);
            model.addAttribute("successMessage", "filters.saved.success");
        }

        model.addAttribute("listOfSavedFiltrationSettings", getListOfSavedFiltrationSettings(principal));
        return "user_filtration_preferences_list";
    }

    @PostMapping("/filtration_preferences/delete")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String deleteUserFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, @RequestParam("setting_key") String key, Model model) {
        if(principal.getUser().getSavedFiltrationSettings().remove(key)!=null){
            model.addAttribute("successMessage", "filters.remove.success");
        }
        model.addAttribute("listOfSavedFiltrationSettings", getListOfSavedFiltrationSettings(principal));
        return "user_filtration_preferences_list";
    }

    private List<Map.Entry<String, FiltrationSettings>> getListOfSavedFiltrationSettings(MyUserPrincipal principal){
        return principal
                .getUser()
                .getSavedFiltrationSettings()
                .entrySet()
                .stream()
                .toList();
    }

    private boolean isFiltrationSettingEmpty(FiltrationSettings filtrationSettings){
        if(filtrationSettings.getAskPriceMax() == null &&
            filtrationSettings.getAskPriceMin() == null &&
            filtrationSettings.getBidPriceMax() == null &&
            filtrationSettings.getBidPriceMin() == null &&
            filtrationSettings.getTradingDateMax() == null &&
            filtrationSettings.getTradingDateMin() == null &&
            filtrationSettings.getEffectiveDateMax() == null &&
            filtrationSettings.getEffectiveDateMin() == null &&
            filtrationSettings.getCurrency().size() == 0){
            return true;
        }
        return false;
    }
}
