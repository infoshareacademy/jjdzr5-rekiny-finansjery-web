package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExchangeRateExtension {
    List<ExchangeRateCurrency> findExchangeRateJoinCurrencyByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable);
}
