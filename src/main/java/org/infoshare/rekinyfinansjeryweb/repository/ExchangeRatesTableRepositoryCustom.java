package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;

import java.util.List;

public interface ExchangeRatesTableRepositoryCustom {
    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings(FiltrationSettings filtrationSettings);
}
