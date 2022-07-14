package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, ExchangeRateExtension {

    @Query("select count(distinct er.date) from ExchangeRate er where (:from is null or er.date>=:from) and (:to is null or er.date<=:to)")
    Optional<Long> countExchangeRatesFromPeriod(LocalDate from, LocalDate to);

    @Query("select er.date from ExchangeRate er where (:from is null or er.date>=:from) and (:to is null or er.date<=:to) group by er.date order by er.date desc")
    List<LocalDate> findExchangeRatesFromPeriod(LocalDate from, LocalDate to, Pageable pageable);

    @Query("select new org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency(er.id, er.date, er.askPrice, er.bidPrice, c.code, c.name, c.category) " +
            "from ExchangeRate er join er.currency c where er.date = :selectedDate order by c.code")
    List<ExchangeRateCurrency> findExchangeRatesCurrenciesByDate(LocalDate selectedDate);

    List<ExchangeRate> findExchangeRatesByCurrency_CodeOrderByDateDesc(String code, Pageable pageable); //TODO use for history view

    List<ExchangeRate> findExchangeRatesByCurrency_CodeAndDateBetweenOrderByDateDesc(String code, LocalDate startDate, LocalDate endDate);

    @Query("select er from ExchangeRate er join er.currency c where c.code in (:codes)")
    List<ExchangeRate> findMultipleExchangeRatesByCodes(List<String> codes, Pageable pageable);
    @Query("select er from ExchangeRate er join er.currency c where " +
            "er.date = (select max(er2.date) from ExchangeRate er2 where er.currency = er2.currency group by er2.currency) " +
            "order by c.code")
    List<ExchangeRate> findCurrentRatesOfEachCurrency();

    @Query(value = "SELECT e FROM ExchangeRate e WHERE e.date = :date")
    List<ExchangeRate> findExchangeRatesByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT e FROM ExchangeRate e WHERE e.currency = :currency AND e.date = :date")
    ExchangeRate findExchangeRateByCurrencyAndDate(@Param("currency") Currency currency, @Param("date") LocalDate date);

}
