package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
