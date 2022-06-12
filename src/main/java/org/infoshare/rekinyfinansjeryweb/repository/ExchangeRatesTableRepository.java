package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRatesTableRepository extends JpaRepository<ExchangeRatesTable, Long>, ExchangeRatesTableRepositoryExtension {

    Page<ExchangeRatesTable> findAllByOrderByEffectiveDateDesc(Pageable pageable);
    /*@Query("SELECT t FROM ExchangeRatesTable t")
    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings();*/

    /*@Query("SELECT t, r, c FROM ExchangeRatesTable t LEFT JOIN t.rates r LEFT JOIN r.currency c WHERE c.code LIKE '%USD%' OR c.code LIKE '%AUD%' ORDER BY t.effectiveDate DESC")
    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings();*/

    /*@Query(nativeQuery = true, value = "SELECT * FROM Exchange_Rates_Table t LEFT JOIN Exchange_Rate r ON r.daily_table = t.id"
            + " LEFT JOIN Currency c ON r.currency_code = c.code"
            + " WHERE c.code LIKE '%USD%' OR c.code LIKE '%AUD%' ORDER BY t.effective_Date DESC LIMIT 5")
    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings();*/

    ExchangeRatesTable findFirstByOrderByEffectiveDateDesc();
}
