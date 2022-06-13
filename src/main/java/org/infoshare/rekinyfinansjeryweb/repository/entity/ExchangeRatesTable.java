package org.infoshare.rekinyfinansjeryweb.repository.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = ExchangeRatesTable.TABLE_NAME)
public class ExchangeRatesTable {
    public static final String TABLE_NAME = "exchange_rates_table";
    public static final String COLUMN_PREFIX = "ert_";
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @NotNull
    @Column(name = COLUMN_PREFIX + "no")
    String no;
    @NotNull
    @Column(name = COLUMN_PREFIX + "effective_date")
    LocalDate effectiveDate;
    @NotNull
    @Column(name = COLUMN_PREFIX + "trading_date")
    LocalDate tradingDate;
    @OneToMany(mappedBy = "dailyTable", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ExchangeRate> rates;
}
