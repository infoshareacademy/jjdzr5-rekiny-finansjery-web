package org.infoshare.rekinyfinansjeryweb.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class FiltrationSettings {
    public FiltrationSettings(){
        currency = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private LocalDate dateMin;
    private LocalDate dateMax;
    private Double bidPriceMin;
    private Double bidPriceMax;
    private Double askPriceMin;
    private Double askPriceMax;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "filt_currency", joinColumns = @JoinColumn(name = "id"))
    private List<String> currency;
}