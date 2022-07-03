package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.user.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.PayMethodFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SaveOfFiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.service.CurrentRatesService;
import org.infoshare.rekinyfinansjeryweb.service.SearchAndFiltrationService;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import org.infoshare.rekinyfinansjeryweb.dto.user.AmountFormDTO;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@Secured({"ROLE_USER", "ROLE_ADMIN"})

public class UserController {

    @Autowired
    UserService usersService;
    @Autowired
    SearchAndFiltrationService searchAndFiltrationService;
    @Autowired
    CurrentRatesService currentRatesService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("user", usersService.getUserDTO());
    }

    @GetMapping
    public String getUser(Model model) {
        model.addAttribute("myCurrencies", usersService.getMyCurrencies());
        return "user";
    }

    @GetMapping("/filtration_preferences")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String userFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, Model model) {
        model.addAttribute("listOfSavedFiltrationSettings", usersService.getListOfSavedFiltrationSettings());
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
                                               @ModelAttribute @Valid SaveOfFiltrationSettingsDTO filtrationSettings,
                                               BindingResult result, Model model, @Autowired UsedCurrenciesService usedCurrenciesService) {
        if(result.hasErrors()){
            model.addAttribute("possibleCurrencies",
                    usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), filtrationSettings.getCurrency()));
            return "user_filtration_preferences_add_form";
        }
        else if(usersService.getSavedFiltrationSettings().containsKey(filtrationSettings.getPreferenceName())){
            model.addAttribute("errorMessage", "filters.error.name.used");
            model.addAttribute("possibleCurrencies",
                    usedCurrenciesService.getShortNamesOfCurrencies(NBPApiManager.getInstance(), filtrationSettings.getCurrency()));
            return "user_filtration_preferences_add_form";
        }

        if(isFiltrationSettingEmpty(filtrationSettings)){
            model.addAttribute("errorMessage", "filters.error.empty");
        }
        else if (usersService.savedFiltrationSettings(filtrationSettings)){
            model.addAttribute("successMessage", "filters.saved.success");
        }

        model.addAttribute("listOfSavedFiltrationSettings", usersService.getListOfSavedFiltrationSettings());
        return "user_filtration_preferences_list";
    }

    @PostMapping("/filtration_preferences/delete")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String deleteUserFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, @RequestParam("setting_key") String key, Model model) {

        if(usersService.removeSavedFiltrationSettings(key)){
            model.addAttribute("successMessage", "filters.remove.success");
        }
        model.addAttribute("listOfSavedFiltrationSettings", usersService.getListOfSavedFiltrationSettings());
        return "user_filtration_preferences_list";
    }

    private boolean isFiltrationSettingEmpty(FiltrationSettingsDTO filtrationSettings) {
        if (filtrationSettings.getAskPriceMax() == null &&
                filtrationSettings.getAskPriceMin() == null &&
                filtrationSettings.getBidPriceMax() == null &&
                filtrationSettings.getBidPriceMin() == null &&
                filtrationSettings.getDateMax() == null &&
                filtrationSettings.getDateMin() == null &&
                filtrationSettings.getCurrency().size() == 0) {
            return true;
        }
        return false;
    }

    @GetMapping("/payment")
    public String getPaymentForm(Model model) {
        model.addAttribute("amount", new PayMethodFormDTO());
        return "pay";
    }

    @PostMapping("/payment")
    public ModelAndView getPayment(@Valid @ModelAttribute("amount") PayMethodFormDTO amount,
                                   BindingResult result,
                                   ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("amount", amount);
            return new ModelAndView("pay",model);
        }
        if (usersService.paymentToWallet(amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.payment");
        } else {
            model.addAttribute("errorMessage", "msg.error.payment");
            //todo LOG
        }
        return new ModelAndView("redirect:/user",model);
    }

    @GetMapping("/withdrawal")
    public String getWithdrawalForm(Model model) {
        PayMethodFormDTO amount = new PayMethodFormDTO();
        amount.setPayMethod("00 0000 0000 0000 0000 0000 0000");
        model.addAttribute("amount", amount);
        return "withdrawal";
    }

    @PostMapping("/withdrawal")
    public ModelAndView getWithdrawal(@Valid @ModelAttribute("amount") PayMethodFormDTO amount,
                                BindingResult result,
                                ModelMap model) {

        if (result.hasErrors()) {
            model.addAttribute("amount", amount);
            return new ModelAndView("withdrawal", model);
        }
        if (usersService.withdrawalFromWallet(amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.payout");
        } else {
            model.addAttribute("errorMessage", "msg.error.payout");
            //todo LOG
        }
        return new ModelAndView("redirect:/user", model);
    }

    @GetMapping("/buycurrency")
    public String getLastTradingTable(Model model) {
        model.addAttribute("exchangeRates", currentRatesService.getLastExchangeRates());
        model.addAttribute("newCurrency", new ExchangeRate());
        return "buycurrency";
    }

    @GetMapping("/buycurrency/{code}")
    public String getBuyCurrency(@PathVariable("code") String code, Model model) {
        ExchangeRateDTO lastCurrencyForTable = currentRatesService.getCurrencyOfLastExchangeRates(code);
        if (lastCurrencyForTable == null) {
            model.addAttribute("errorMessage","msg.error.collectors.currency");
            return "user";
        }
        model.addAttribute("amount", new AmountFormDTO());
        model.addAttribute("rate",lastCurrencyForTable);
        return "ask";
    }

    @PostMapping("/buycurrency/{code}")
    public ModelAndView askCurrency(@PathVariable("code") String code,
                              @Valid @ModelAttribute("amount") AmountFormDTO amount,
                              BindingResult result,
                              ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("rate", currentRatesService.getCurrencyOfLastExchangeRates(code));
            return new ModelAndView("ask",model);
        }
        if (usersService.askCurrency(code, amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.buy.currency");
        } else {
            model.addAttribute("errorMessage", "msg.error.operation");
            //todo LOG
        }
        return new ModelAndView("redirect:/user", model);
    }

    @GetMapping("/sellcurrency/{code}")
    public String getSellCurrency(@PathVariable("code") String code, Model model) {
        ExchangeRateDTO lastCurrencyForTable = currentRatesService.getCurrencyOfLastExchangeRates(code);
        if (lastCurrencyForTable == null) {
            model.addAttribute("errorMessage","msg.error.collectors.currency");
            return "user";
        }
        model.addAttribute("amount", new AmountFormDTO());
        model.addAttribute("rate",lastCurrencyForTable);
        return "bid";
    }

    @PostMapping("/sellcurrency/{code}")
    public ModelAndView bidCurrency(@PathVariable("code") String code,
                              @Valid @ModelAttribute("amount") AmountFormDTO amount,
                              BindingResult result,
                              ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("rate", currentRatesService.getCurrencyOfLastExchangeRates(code));
            return new ModelAndView("bid", model);
        }

        if (usersService.bidCurrency(code,amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.sell.currency");
        } else {
            model.addAttribute("errorMessage", "msg.error.operation");
            //todo LOG
        }
        return new ModelAndView("redirect:/user", model);
    }

    @GetMapping("/history")
    public String getHistoryOperation(Model model) {
        model.addAttribute("operationhistory", usersService.getHistoryOperation());
        return "historyoperation";
    }

    @GetMapping("/history/{code}")
    public String getHistoryOperationCurrency(@PathVariable("code") String code, Model model) {
        model.addAttribute("operationhistory", usersService.getHistoryOperation(code));
        return "historyoperation";
    }
}
