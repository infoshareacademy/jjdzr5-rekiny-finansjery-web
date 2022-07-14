package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;

import java.util.List;

public interface CurrencyRepositoryExtension {
    List<Currency> findAllCurrencyByCodeIn(FiltrationSettingsDTO filtrationSettings);
    List<Currency> findAllCurrencyByCodeIn(SearchSettingsDTO searchSettingsDTO);
}
