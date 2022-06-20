package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiAdapter;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiRequestResult;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExtendedGson;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.data.DailyExchangeRates;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class NBPApiAdapter extends ApiAdapter {

    private final String apiName = "NBP_API_CURRENCY";
    private final String URL_LAST_67_DAYS_TABLES = "https://api.nbp.pl/api/exchangerates/tables/c/last/67/";
    private final String URL_RANGE_OF_DATE = "https://api.nbp.pl/api/exchangerates/tables/c/%1$s/%2$s/";
    private final Integer LIMIT_DAYS = 90;

    public String getApiName() {
        return apiName;
    }

    private List<DailyExchangeRates> fromJson(String response) {
        Gson gson = ExtendedGson.getExtendedGson();
        return gson.fromJson(response, new TypeToken<ArrayList<DailyExchangeRates>>(){}.getType());
    }

    private ApiRequestResult convertToDatabaseStructures(List<DailyExchangeRates> data, List<Currency> currencies){
        Map<String, Currency> currenciesMap = currencyListToMap(currencies);
        ApiRequestResult result = new ApiRequestResult();

        for(DailyExchangeRates dailyExchangeRate : data){
            convertDailyExchangeRatesToDatabase(dailyExchangeRate, currenciesMap, result);
        }
        return result;
    }

    private void convertDailyExchangeRatesToDatabase(DailyExchangeRates dailyExchangeRate, Map<String, Currency> currenciesMap, ApiRequestResult result){
        dailyExchangeRate.getRates().forEach(exchangeRate -> {
            Optional<Currency> currency = Optional.ofNullable(currenciesMap.get(exchangeRate.getCode()));
            if(currency.isEmpty()){
                Currency newCurrency = new Currency(null, exchangeRate.getCode(),
                    exchangeRate.getCurrency(), "currency", new ArrayList<>());
                currenciesMap.put(newCurrency.getCode(), newCurrency);
                result.getCurrencies().add(newCurrency);
                currency = Optional.of(newCurrency);
            }
            ExchangeRate rate = new ExchangeRate(null, currency.get(), dailyExchangeRate.getEffectiveDate(), exchangeRate.getAsk(), exchangeRate.getBid());
            result.getExchangeRates().add(rate);
        });
    }

    private Map<String, Currency> currencyListToMap(List<Currency> currencies){
        Map<String, Currency> map = currencies.stream()
                .collect(Collectors.toMap(Currency::getCode, item -> item));
        return map;
    }

    public ApiRequestResult getResultData(List<Currency> currencies, Optional<LastUpdate> lastUpdate){
        if(lastUpdate.isPresent()){
            return  getDaysFrom(currencies, lastUpdate);
        }
        return getLastDays(currencies);
    }

    private ApiRequestResult getLastDays(List<Currency> currencies){
        String response = getDataFromApi(URL_LAST_67_DAYS_TABLES);
        List<DailyExchangeRates> data = fromJson(response);
        return convertToDatabaseStructures(data, currencies);
    }

    private ApiRequestResult getDaysFrom(List<Currency> currencies, Optional<LastUpdate> lastUpdate){
        List<DailyExchangeRates> data = new CopyOnWriteArrayList<>();
        LocalDate startDate = lastUpdate.get().getUpdateTime().toLocalDate().plusDays(1);
        LocalDate endDate = LocalDate.now();
        LocalDate tempEndDate = (ChronoUnit.DAYS.between(startDate, endDate) > LIMIT_DAYS? startDate.plusDays(LIMIT_DAYS):endDate);
        do {
            try {
                data.addAll(fromJson(getDataFromApi(String.format(URL_RANGE_OF_DATE, startDate, tempEndDate))));
            } catch (NullPointerException e) {
                //TODO add logger
                //LOGGER.info("No data to download.");
            }
            startDate = tempEndDate;
            tempEndDate = (ChronoUnit.DAYS.between(startDate, endDate) > LIMIT_DAYS? startDate.plusDays(LIMIT_DAYS):endDate);
        } while (endDate.isAfter(startDate));
        return convertToDatabaseStructures(data, currencies);
    }
}
