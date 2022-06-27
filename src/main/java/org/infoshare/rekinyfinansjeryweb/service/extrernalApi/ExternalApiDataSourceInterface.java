package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExternalApiDataSourceInterface {
    String getApiName();
    ApiRequestResult getResultData(List<Currency> currencies, Optional<LastUpdate> lastUpdate);
}
