package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class FiltrationSettingsDTO {
    public FiltrationSettingsDTO (){
        currency = new ArrayList<>();
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDateMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDateMax;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradingDateMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradingDateMax;
    private Double bidPriceMin;
    private Double bidPriceMax;
    private Double askPriceMin;
    private Double askPriceMax;
    private List<String> currency;
}