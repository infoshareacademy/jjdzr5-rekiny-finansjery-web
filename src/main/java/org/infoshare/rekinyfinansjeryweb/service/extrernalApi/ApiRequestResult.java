package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiRequestResult {
    public ApiRequestResult(){
        currencies = new ArrayList<>();
        exchangeRates = new ArrayList<>();
        exchangeRatesTables = new ArrayList<>();
    }
    private List<Currency> currencies;
    private List<ExchangeRatesTable> exchangeRatesTables;
    private List<ExchangeRate> exchangeRates;
}
