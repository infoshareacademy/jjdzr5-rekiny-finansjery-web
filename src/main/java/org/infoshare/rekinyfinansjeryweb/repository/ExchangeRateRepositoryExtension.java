package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExchangeRateRepositoryExtension {
    Long countDatesBySearchSettings(SearchSettingsDTO searchSettings, List<Currency> fittingCurrenciesIds);
    Long countDatesByFilterSettings(FiltrationSettingsDTO filtrationSettings, List<Currency> fittingCurrenciesIds);
    List<LocalDate> findDatesFromPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable, List<Currency> fittingCurrenciesIds);
    List<LocalDate> findDatesFromPageBySearchSettings(SearchSettingsDTO searchSettings, Pageable pageable, List<Currency> fittingCurrenciesIds);
    List<ExchangeRateCurrency> findSelectedDates(FiltrationSettingsDTO filtrationSettings, List<LocalDate> dates, List<Currency> fittingCurrenciesIds);
    List<ExchangeRateCurrency> findSelectedDates(SearchSettingsDTO searchSettings, List<LocalDate> dates, List<Currency> fittingCurrenciesIds);
}
