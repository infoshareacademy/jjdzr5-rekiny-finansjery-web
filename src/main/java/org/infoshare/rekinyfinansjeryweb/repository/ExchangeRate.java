package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Currency currency;
    @ManyToOne
    private ExchangeRatesTable table;
    private Double askPrice;
    private Double bidPrice;
}
