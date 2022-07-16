package org.infoshare.rekinyfinansjeryweb.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.infoshare.rekinyfinansjeryweb.controller.controllerComponents.ListToPagesSplitter;
import org.infoshare.rekinyfinansjeryweb.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/currency")
@RequiredArgsConstructor

public class CurrencyChartController {

    @Autowired
    ChartService chartService;

    @GetMapping("/{code}")
    public String showChart(@PathVariable("code") String code, Model model) {

        List<ChartService.ChartData> chartData = chartService.getChartData(code);
        model.addAttribute("chartData", chartData);
        return "chart";
    }

    @GetMapping("/{code}/monthly")
    public String showChart(@PathVariable("code") String code, @RequestParam("year") int year, @RequestParam("month") int month, Model model) {

        List<ChartService.ChartData> chartData = chartService.getChartData(code, year, month);
        model.addAttribute("chartData", chartData);
        return "chart";
    }

    @GetMapping("/history/{code}")
    public String showHistory(@PathVariable("code") String code, Model model, @PageableDefault(size = 25) Pageable pageable) {

        List<ChartService.ChartData> chartData = chartService.sendExchangeRatesForGivenPage(code, pageable);
        ListToPagesSplitter.splitIntoPages(chartData, model, pageable);
        return "history";
    }

    @ControllerAdvice
    public class ControllerExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorCause> handle(Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorCause(e.getMessage()));
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class ErrorCause {
        private String cause;
    }

}