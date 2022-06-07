package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTableExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;

import java.util.List;

public interface ExchangeRatesTableRepositoryExtension {
    List<ExchangeRatesTableExchangeRateCurrency> findExchangeRatesTableByFilterSettings(FiltrationSettingsDTO filtrationSettings);
}
