package org.infoshare.rekinyfinansjeryweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Query("SELECT t FROM ExchangeRate r JOIN ExchangeRatesTable t JOIN Currency c")
    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings();
}
