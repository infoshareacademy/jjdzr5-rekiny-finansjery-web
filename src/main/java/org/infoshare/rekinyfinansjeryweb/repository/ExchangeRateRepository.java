package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, ExchangeRateExtension {

    @Query("select count(distinct er.date) from ExchangeRate er where (:from is null or er.date>=:from) and (:to is null or er.date<=:to)")
    Optional<Long> countExchangeRatesFromPeriod(LocalDate from, LocalDate to);

    @Query("select er.date from ExchangeRate er where (:from is null or er.date>=:from) and (:to is null or er.date<=:to) group by er.date order by er.date desc")
    List<LocalDate> findExchangeRatesFromPeriod(LocalDate from, LocalDate to, Pageable pageable);

    @Query("select new org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency(er.id, er.date, er.askPrice, er.bidPrice, c.code, c.name, c.category) " +
            "from ExchangeRate er join er.currency c where er.date = :selectedDate order by c.code")
    List<ExchangeRateCurrency> findExchangeRatesByDate(LocalDate selectedDate);

    List<ExchangeRate> findExchangeRatesByCurrency_CodeOrderByDateDesc(String code, Pageable pageable); //TODO use for history view

    List<ExchangeRate> findExchangeRatesByCurrency_CodeAndDateBetweenOrderByDateDesc(String code, LocalDate startDate, LocalDate endDate);

    @Query("select er from ExchangeRate er join er.currency c where c.code in (:codes)")
    List<ExchangeRate> findMultipleExchangeRatesByCodes(List<String> codes, Pageable pageable);

}
