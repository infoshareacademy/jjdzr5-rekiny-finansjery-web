package org.infoshare.rekinyfinansjeryweb.formData;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class FiltrationSettings{
    public FiltrationSettings(){
        currency = new ArrayList<>();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "filt_currency", joinColumns = @JoinColumn(name = "id"))
    private List<String> currency;
}