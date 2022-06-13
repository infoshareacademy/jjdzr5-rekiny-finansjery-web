package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.repository.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.repository.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.repository.entity.ExchangeRatesTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query(value = "SELECT e FROM ExchangeRate e WHERE e.currency = :currency AND e.dailyTable = :table")
    ExchangeRate findByCurrencyAndTable(@Param("currency") Currency currency, @Param("table") ExchangeRatesTable table);
}
