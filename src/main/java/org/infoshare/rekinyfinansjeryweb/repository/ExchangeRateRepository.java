package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, ExchangeRateExtension {
    @Query("select count(er) from ExchangeRate er where (:from is null or er.date>=:from) and (:from is null or er.date<=:to)")
    List<ExchangeRate> countExchangeRatesInPeriod(LocalDate from, LocalDate to);
}
