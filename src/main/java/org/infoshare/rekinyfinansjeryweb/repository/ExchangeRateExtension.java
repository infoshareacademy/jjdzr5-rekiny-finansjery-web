package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateExtension {
    List<ExchangeRateCurrency> findExchangeRateJoinCurrencyByFilterSettings(FiltrationSettingsDTO filtrationSettings, List<LocalDate> requestedDates);
    //List<ExchangeRateCurrency> findExchangeRateJoinCurrencyByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable);

}
