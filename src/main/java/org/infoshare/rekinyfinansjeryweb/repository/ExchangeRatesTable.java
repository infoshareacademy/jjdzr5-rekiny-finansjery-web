package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
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
}
