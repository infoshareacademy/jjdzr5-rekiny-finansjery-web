package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRatesTable;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiAdapter;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiRequestResult;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExtendedGson;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.data.DailyExchangeRates;
import org.infoshare.rekinyfinansjeryweb.repository.Currency;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class NBPApiAdapter extends ApiAdapter {

    private final String URL_LAST_67_DAYS_TABLES = "https://api.nbp.pl/api/exchangerates/tables/c/last/67/";
    private final String URL_RANGE_OF_DATE = "http://api.nbp.pl/api/exchangerates/tables/c/%1$s/%2$s/";
    private final Integer LIMIT_DAYS = 90;

    private List<DailyExchangeRates> fromJson(String response) {
        Gson gson = ExtendedGson.getExtendedGson();
        return gson.fromJson(response, new TypeToken<ArrayList<DailyExchangeRates>>(){}.getType());
    }

    private ApiRequestResult convertToDatabaseStructures(List<DailyExchangeRates> list){
        Map<String, Currency> currenciesMap = new HashMap<>();
        List<ExchangeRatesTable> exchangeRatesTables = new ArrayList<>();
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for(DailyExchangeRates dailyExchangeRate : list){
            ExchangeRatesTable exchangeRatesTable = new ExchangeRatesTable(null, dailyExchangeRate.getNo(),
                    dailyExchangeRate.getEffectiveDate(), dailyExchangeRate.getTradingDate(), null);
            dailyExchangeRate.getRates().forEach(exchangeRate -> {
                Currency currency = new Currency(exchangeRate.getCode(),
                        exchangeRate.getCurrency(),
                        "currency");
                currenciesMap.putIfAbsent(exchangeRate.getCode(), currency);
                currency = currenciesMap.getOrDefault(exchangeRate.getCode(), currency);
                ExchangeRate rate = new ExchangeRate(null, currency, exchangeRatesTable, exchangeRate.getAsk(), exchangeRate.getBid());
                exchangeRates.add(rate);
            });
            exchangeRatesTables.add(exchangeRatesTable);
        }

        return new ApiRequestResult(currenciesMap.values().stream().collect(Collectors.toList()), exchangeRatesTables, exchangeRates);
    }

    public ApiRequestResult getResultData(){
        String response = getDataFromApi(URL_LAST_67_DAYS_TABLES);
        List<DailyExchangeRates> list = fromJson(response);
        return convertToDatabaseStructures(list);
    }
}
