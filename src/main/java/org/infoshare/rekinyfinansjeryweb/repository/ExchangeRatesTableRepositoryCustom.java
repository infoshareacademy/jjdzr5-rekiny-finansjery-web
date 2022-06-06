package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;

import java.util.List;

public interface ExchangeRatesTableRepositoryCustom {
    List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings(FiltrationSettings filtrationSettings);
}
