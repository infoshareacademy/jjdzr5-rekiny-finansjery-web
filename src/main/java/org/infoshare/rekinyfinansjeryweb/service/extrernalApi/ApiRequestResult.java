package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.infoshare.rekinyfinansjeryweb.repository.Currency;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRatesTable;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiRequestResult {
    private List<Currency> currencies;
    private List<ExchangeRatesTable> exchangeRatesTables;
    private List<ExchangeRate> exchangeRates;
}
