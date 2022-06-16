package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExchangeRateExtension {
    Long countDatesByFilterSettings(FiltrationSettingsDTO filtrationSettings);
    List<LocalDate> findDatesFromPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable);
    List<ExchangeRateCurrency> findPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, List<LocalDate> dates);
}
