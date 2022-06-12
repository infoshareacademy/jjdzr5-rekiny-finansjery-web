package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTableExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExchangeRatesTableRepositoryExtension {

    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings(FiltrationSettingsDTO filtrationSettings);
    List<ExchangeRatesTableExchangeRateCurrency> findExchangeRatesTableJoinExchangeRateJoinCurrencyByFilterSettings(List<String> tables, FiltrationSettingsDTO filtrationSettings, Pageable pageable);
}
