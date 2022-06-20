package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, ExchangeRateExtension {
    @Query("select count(er) from ExchangeRate er where (:from is null or er.date>=:from) and (:from is null or er.date<=:to)")
    List<ExchangeRate> countExchangeRatesInPeriod(LocalDate from, LocalDate to);

    @Query(value = "SELECT e FROM ExchangeRate e WHERE e.date = :date")
    List<ExchangeRate> findExchangeRatesByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT e FROM ExchangeRate e WHERE e.currency = :currency AND e.date = :date")
    ExchangeRate findExchangeRateByCurrencyAndDate(@Param("currency") Currency currency, @Param("date") LocalDate date);
}
