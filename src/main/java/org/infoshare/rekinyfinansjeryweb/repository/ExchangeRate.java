package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "dailyTable")
    private ExchangeRatesTable dailyTable;
    private Double askPrice;
    private Double bidPrice;
}
