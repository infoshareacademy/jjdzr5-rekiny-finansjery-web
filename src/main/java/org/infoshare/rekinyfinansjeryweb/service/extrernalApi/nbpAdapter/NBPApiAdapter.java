package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiRequestResult;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.CurrencyTagGenerator;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExternalApiDataSourceInterface;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.nbpAdapter.dto.DailyExchangeRates;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class NBPApiAdapter implements ExternalApiDataSourceInterface {

    CurrencyTagGenerator currencyTagGenerator;
    RestTemplate restTemplate;

    public NBPApiAdapter(CurrencyTagGenerator currencyTagGenerator, RestTemplate restTemplate) {
        this.currencyTagGenerator = currencyTagGenerator;
        this.restTemplate = restTemplate;
    }

    private static final String apiName = "NBP_API_CURRENCY";
    private static final String URL_LAST_67_DAYS_TABLES = "https://api.nbp.pl/api/exchangerates/tables/c/last/67/";
    private static final String URL_RANGE_OF_DATE = "https://api.nbp.pl/api/exchangerates/tables/c/{start}/{end}/";
    private static  final Integer LIMIT_DAYS = 90;

    public String getApiName() {
        return apiName;
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
                    exchangeRate.getCurrency(), "currency", new ArrayList<>(), currencyTagGenerator.createTag(exchangeRate.getCode(), exchangeRate.getCurrency()));
                currenciesMap.put(newCurrency.getCode(), newCurrency);
                result.getCurrencies().add(newCurrency);
                currency = Optional.of(newCurrency);
            }
            ExchangeRate rate = new ExchangeRate(null, currency.get(), dailyExchangeRate.getEffectiveDate(), exchangeRate.getAsk(), exchangeRate.getBid());
            result.getExchangeRates().add(rate);
        });
    }

    private Map<String, Currency> currencyListToMap(List<Currency> currencies){
        return currencies.stream()
                .collect(Collectors.toMap(Currency::getCode, item -> item));
    }

    public ApiRequestResult getResultData(List<Currency> currencies, Optional<LastUpdate> lastUpdate){
        if(lastUpdate.isPresent()){
            return  getDaysFrom(currencies, lastUpdate);
        }
        return getLastDays(currencies);
    }

    private ApiRequestResult getLastDays(List<Currency> currencies){
        List<DailyExchangeRates> data = Arrays.stream(Optional.
                ofNullable(restTemplate.getForObject(URL_LAST_67_DAYS_TABLES, DailyExchangeRates[].class)).
                orElse(new DailyExchangeRates[0])).toList();
        return convertToDatabaseStructures(data, currencies);
    }

    private ApiRequestResult getDaysFrom(List<Currency> currencies, Optional<LastUpdate> lastUpdate){
        List<DailyExchangeRates> data = new CopyOnWriteArrayList<>();
        LocalDate startDate = lastUpdate.get().getUpdateTime().toLocalDate().plusDays(1);
        LocalDate endDate = LocalDate.now();

        if(endDate.isBefore(startDate)){
            return new ApiRequestResult();
        }

        LocalDate tempEndDate = (ChronoUnit.DAYS.between(startDate, endDate) > LIMIT_DAYS? startDate.plusDays(LIMIT_DAYS):endDate);
        do {
            List<DailyExchangeRates> nextPackage = Arrays.stream(Optional.
                    ofNullable(restTemplate.getForObject(URL_RANGE_OF_DATE, DailyExchangeRates[].class, startDate, endDate)).
                    orElse(new DailyExchangeRates[0])).toList();
            data.addAll(nextPackage);
            startDate = tempEndDate;
            tempEndDate = (ChronoUnit.DAYS.between(startDate, endDate) > LIMIT_DAYS? startDate.plusDays(LIMIT_DAYS):endDate);
        } while (endDate.isAfter(startDate));
        return convertToDatabaseStructures(data, currencies);
    }
}
