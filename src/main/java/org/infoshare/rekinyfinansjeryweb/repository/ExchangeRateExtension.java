package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.entity.LastRating;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateExtension {
    Long countDatesBySearchSettings(SearchSettingsDTO searchSettings);
    Long countDatesByFilterSettings(FiltrationSettingsDTO filtrationSettings);
    List<LocalDate> findDatesFromPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable);
    List<LocalDate> findDatesFromPageBySearchSettings(SearchSettingsDTO searchSettings, Pageable pageable);
    List<ExchangeRateCurrency> findPageBySearchSettings(FiltrationSettingsDTO filtrationSettings, List<LocalDate> dates);

    List<ExchangeRateCurrency> findPageBySearchSettings(SearchSettingsDTO searchSettings, List<LocalDate> dates);

    List<LastRating> findLastRatingDateForEachCurrency();
}
