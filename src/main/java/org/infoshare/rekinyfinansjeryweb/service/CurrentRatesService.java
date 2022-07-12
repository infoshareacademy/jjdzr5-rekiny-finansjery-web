package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.DailyTableDTO;
import org.infoshare.rekinyfinansjeryweb.dto.ExchangeRateDTO;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CurrentRatesService {
    ExchangeRateRepository exchangeRateRepository;

    public CurrentRatesService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public ExchangeRateDTO getCurrencyOfLastExchangeRates(String currency){
        return getLastExchangeRates().getRates()
                .stream()
                .filter(e -> e.getCode().equals(currency))
                .findFirst().orElse(new ExchangeRateDTO());
    }

    public DailyTableDTO getLastExchangeRates(){
        List<ExchangeRate> exchangeRates =  exchangeRateRepository.findCurrentRatesOfEachCurrency();
        DailyTableDTO dailyTableDTO = new DailyTableDTO();
        dailyTableDTO.setDate(LocalDate.now());
        dailyTableDTO.setRates(
                exchangeRates
                        .stream()
                        .map( e -> new ExchangeRateDTO(e.getAskPrice(), e.getBidPrice(), e.getCurrency().getCode(), e.getCurrency().getName(), e.getCurrency().getCategory()))
                        .toList());
        return dailyTableDTO;
    }
}
