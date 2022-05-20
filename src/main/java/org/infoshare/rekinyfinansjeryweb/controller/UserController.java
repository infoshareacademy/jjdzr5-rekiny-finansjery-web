package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.data.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
import org.infoshare.rekinyfinansjeryweb.formData.SaveOfFiltrationSettings;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;
import com.infoshareacademy.domain.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.formData.AmountForm;
import org.infoshare.rekinyfinansjeryweb.service.SearchService;
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
@Secured({"ROLE_USER", "ROLE_ADMIN"})

public class UserController {

    @Autowired
    UserService usersService;
    @Autowired
    SearchService searchService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("user", usersService.getUser());
    }

    @GetMapping
    public String getUser(Model model) {
        return "user";
    }

    @GetMapping("/filtration_preferences")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String userFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, Model model) {
        model.addAttribute("listOfSavedFiltrationSettings", usersService.getListOfSavedFiltrationSettings(principal));
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

        model.addAttribute("listOfSavedFiltrationSettings", usersService.getListOfSavedFiltrationSettings(principal));
        return "user_filtration_preferences_list";
    }

    @PostMapping("/filtration_preferences/delete")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String deleteUserFiltrationPreferences(@AuthenticationPrincipal MyUserPrincipal principal, @RequestParam("setting_key") String key, Model model) {
        if(principal.getUser().getSavedFiltrationSettings().remove(key)!=null){
            model.addAttribute("successMessage", "filters.remove.success");
        }
        model.addAttribute("listOfSavedFiltrationSettings", usersService.getListOfSavedFiltrationSettings(principal));
        return "user_filtration_preferences_list";
    }



    private boolean isFiltrationSettingEmpty(FiltrationSettings filtrationSettings) {
        if (filtrationSettings.getAskPriceMax() == null &&
                filtrationSettings.getAskPriceMin() == null &&
                filtrationSettings.getBidPriceMax() == null &&
                filtrationSettings.getBidPriceMin() == null &&
                filtrationSettings.getTradingDateMax() == null &&
                filtrationSettings.getTradingDateMin() == null &&
                filtrationSettings.getEffectiveDateMax() == null &&
                filtrationSettings.getEffectiveDateMin() == null &&
                filtrationSettings.getCurrency().size() == 0) {
            return true;
        }
        return false;
    }

    @GetMapping("/payment")
    public String getPaymentForm(Model model) {
        model.addAttribute("payment_method", "credit_card");
        model.addAttribute("amount", new AmountForm());
        return "pay";
    }

    @PostMapping("/payment")
    public String getPayment(@ModelAttribute("payment_method") String paymentMethod,
                             @Valid @ModelAttribute("amount") AmountForm amount,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("payment_method", paymentMethod);
            model.addAttribute("amount", amount);
            return "pay";
        }
        if (usersService.paymentToWallet(amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.payment");
        } else {
            model.addAttribute("errorMessage", "msg.error.payment");
            //todo LOG
        }
        return "user";
    }

    @GetMapping("/withdrawal")
    public String getWithdrawalForm(Model model) {
        model.addAttribute("amount", new AmountForm());
        model.addAttribute("bank_account_number", "00 0000 0000 0000 0000 0000 0000");
        return "withdrawal";
    }

    @PostMapping("/withdrawal")
    public String getWithdrawal(@ModelAttribute("bank_account_number") String bankAccountNumber,
                                @Valid @ModelAttribute("amount") AmountForm amount,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("payment_method", bankAccountNumber);
            model.addAttribute("amount", amount);
            return "withdrawal";
        }
        if (usersService.withdrawalFromWallet(amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.payout");
        } else {
            model.addAttribute("errorMessage", "msg.error.payout");
            //todo LOG
        }
        return "user";
    }

    @GetMapping("/buycurrency")
    public String getLastTradingTable(Model model) {
        model.addAttribute("exchangeRates", searchService.getLastExchangeRates());
        model.addAttribute("newCurrency", new ExchangeRate());
        return "buycurrency";
    }

    @GetMapping("/buycurrency/{code}")
    public String getBuyCurrency(@PathVariable("code") String code, Model model) {
        ExchangeRate lastCurrencyForTable = searchService.getCurrencyOfLastExchangeRates(code);
        if (lastCurrencyForTable == null) {
            model.addAttribute("errorMessage","msg.error.collectors.currency");
            return "user";
        }
        model.addAttribute("amount", new AmountForm());
        model.addAttribute("rate",lastCurrencyForTable);
        return "ask";
    }

    @PostMapping("/buycurrency/{code}")
    public String askCurrency(@PathVariable("code") String code,
                              @Valid @ModelAttribute("amount") AmountForm amount,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rate", searchService.getCurrencyOfLastExchangeRates(code));
            return "ask";
        }
        if (usersService.askCurrency(code, amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.buy.currency");
        } else {
            model.addAttribute("errorMessage", "msg.error.operation");
            //todo LOG
        }
        return "user";
    }

    @GetMapping("/sellcurrency/{code}")
    public String getSellCurrency(@PathVariable("code") String code, Model model) {
        ExchangeRate lastCurrencyForTable = searchService.getCurrencyOfLastExchangeRates(code);
        if (lastCurrencyForTable == null) {
            model.addAttribute("errorMessage","msg.error.collectors.currency");
            return "user";
        }
        model.addAttribute("amount", new AmountForm());
        model.addAttribute("rate",lastCurrencyForTable);
        return "bid";
    }

    @PostMapping("/sellcurrency/{code}")
    public String bidCurrency(@PathVariable("code") String code,
                              @Valid @ModelAttribute("amount") AmountForm amount,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rate", searchService.getCurrencyOfLastExchangeRates(code));
            return "bid";
        }

        if (usersService.bidCurrency(code,amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.sell.currency");
        } else {
            model.addAttribute("errorMessage", "msg.error.operation");
            //todo LOG
        }
        return "user";
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
