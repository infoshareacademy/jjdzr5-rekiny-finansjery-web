package org.infoshare.rekinyfinansjeryweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRatesTableExchangeRateCurrency {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = ExchangeRatesTable.COLUMN_PREFIX + "id")
    private UUID id;
    @Column(name = ExchangeRatesTable.COLUMN_PREFIX + "no")
    String no;
    @Column(name = ExchangeRatesTable.COLUMN_PREFIX + "effective_date")
    LocalDate effectiveDate;

    @Column(name = ExchangeRate.COLUMN_PREFIX + "ask_price")
    private Double askPrice;
    @Column(name = ExchangeRate.COLUMN_PREFIX + "bid_price")
    private Double bidPrice;

    @Column(name = Currency.COLUMN_PREFIX + "code")
    String code;
    @Column(name = Currency.COLUMN_PREFIX + "name")
    String name;
    @Column(name = Currency.COLUMN_PREFIX + "category")
    String category;
}
