package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRatesTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    String no;
    @NotNull
    LocalDate effectiveDate;
    @NotNull
    LocalDate tradingDate;
    @OneToMany(mappedBy = "dailyTable")
    List<ExchangeRate> rates;
}
