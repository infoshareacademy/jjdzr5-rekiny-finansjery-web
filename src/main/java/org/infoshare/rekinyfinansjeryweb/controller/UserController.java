package org.infoshare.rekinyfinansjeryweb.controller;

import com.infoshareacademy.domain.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.form.AmountForm;
import org.infoshare.rekinyfinansjeryweb.service.SearchService;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return "redirect:/user";
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
        return "redirect:/user";
    }

    @GetMapping("/buycurrency")
    public String getLastTradingTable(Model model) {
        model.addAttribute("exchangeRates", searchService.getLastExchangeRates());
        model.addAttribute("newCurrency", new ExchangeRate());
        return "buycurrency";
    }

    @GetMapping("/buycurrency/{code}")
    public String getBuyCurrency(@PathVariable("code") String code, Model model) {
        ExchangeRate lastCurrencyForTable = searchService.getCurrencyOfLastExchamgeRates(code);
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
            model.addAttribute("rate", searchService.getCurrencyOfLastExchamgeRates(code));
            return "ask";
        }
        if (usersService.askCurrency(code, amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.buy.currency");
        } else {
            model.addAttribute("errorMessage", "msg.error.operation");
            //todo LOG
        }
        return "redirect:/user";
    }

    @GetMapping("/sellcurrency/{code}")
    public String getSellCurrency(@PathVariable("code") String code, Model model) {
        ExchangeRate lastCurrencyForTable = searchService.getCurrencyOfLastExchamgeRates(code);
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
            model.addAttribute("rate", searchService.getCurrencyOfLastExchamgeRates(code));
            return "bid";
        }

        if (usersService.bidCurrency(code,amount.getAmount())) {
            model.addAttribute("successMessage", "msg.success.sell.currency");
        } else {
            model.addAttribute("errorMessage", "msg.error.operation");
            //todo LOG
        }
        return "redirect:/user";
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
