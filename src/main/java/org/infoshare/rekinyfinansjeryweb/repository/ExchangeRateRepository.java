package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>, ExchangeRateExtension {
    @Query("select count(distinct er.date) from  ExchangeRate er where (:from is null or er.date>=:from) and (:to is null or er.date<=:to)")
    Optional<Long> countExchangeRatesFromPeriod(LocalDate from, LocalDate to);
    @Query("select er.date from ExchangeRate er where (:from is null or er.date>=:from) and (:to is null or er.date<=:to) group by er.date order by er.date desc")
    List<LocalDate> findExchangeRatesFromPeriod(LocalDate from, LocalDate to, Pageable pageable);
}
