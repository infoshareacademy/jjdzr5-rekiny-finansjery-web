package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ApiRequestResult;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExternalApiDataSourceInterface;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko.dto.CoinData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CoinGeckoAdapter implements ExternalApiDataSourceInterface {

    RestTemplate restTemplate;

    private final String id;

    private final String name;
    private static final String URL_COIN_HISTORY = "https://api.coingecko.com/api/v3/coins/{id}/history?date={date}&localization=false";
    private static  final Integer LIMIT_DAYS = 10;

    public CoinGeckoAdapter(RestTemplate restTemplate, String id, String name) {
        this.restTemplate = restTemplate;
        this.id = id;
        this.name = name;
    }

    @Override
    public String getApiName() {
        return name;
    }

    @Override
    public ApiRequestResult getResultData(List<Currency> currencies, Optional<LastUpdate> lastUpdate) {
        List<CoinData> coinData = sendRequests(lastUpdate);
        return convertToDatabaseStructures(coinData, currencies);
    }

    private List<CoinData> sendRequests(Optional<LastUpdate> lastUpdate){
        LocalDate startDate = lastUpdate.map(update -> update.getUpdateTime().toLocalDate()).orElse(LocalDate.now().minusDays(LIMIT_DAYS));
        LocalDate endDate = LocalDate.now();
        List<CoinData> coinDataList = new ArrayList<>();

        LocalDate nextDate = startDate;
        while(nextDate.isBefore(endDate)) {
            Optional<CoinData> coinData = Optional
                    .ofNullable(restTemplate.getForObject(URL_COIN_HISTORY, CoinData.class, id, nextDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

            LocalDate finalNextDate = nextDate;
            coinData.ifPresent(data -> {
                data.setDate(finalNextDate);
                coinDataList.add(data);
            });
            nextDate = nextDate.plusDays(1);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        return coinDataList;
    }

    private ApiRequestResult convertToDatabaseStructures(List<CoinData> coinData, List<Currency> currencies){
        Map<String, Currency> currenciesMap = currencyListToMap(currencies);
        ApiRequestResult result = new ApiRequestResult();

        coinData.forEach(data -> {
            Optional<Currency> currency = Optional.ofNullable(currenciesMap.get(data.getSymbol().toUpperCase()));
            if(currency.isEmpty()){
                Currency newCurrency = new Currency(null, data.getSymbol().toUpperCase(),
                        data.getName(), "cryptocurrency");
                currenciesMap.put(newCurrency.getCode().toUpperCase(), newCurrency);
                result.getCurrencies().add(newCurrency);
                currency = Optional.of(newCurrency);
            }
            ExchangeRate rate =
                    new ExchangeRate(null, currency.get(), data.getDate(), data.getMarketData().getCurrentPrice().getPln(),
                            data.getMarketData().getCurrentPrice().getPln());
            result.getExchangeRates().add(rate);
        });

        return result;
    }

    private Map<String, Currency> currencyListToMap(List<Currency> currencies){
        return currencies.stream()
                .collect(Collectors.toMap(Currency::getCode, item -> item));
    }
}
