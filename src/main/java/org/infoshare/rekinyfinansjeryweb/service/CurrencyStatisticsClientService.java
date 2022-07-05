package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchedCurrenciesListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CurrencyStatisticsClientService {

    @Autowired
    RestTemplate restTemplate;

    public List<CurrencyStatisticsDTO> getAllResults() {

        return Arrays.stream(Optional.ofNullable(restTemplate
                .getForObject("http://localhost:8081/api/all", CurrencyStatisticsDTO[].class))
                .orElse(new CurrencyStatisticsDTO[0])).toList();
    }

    public List<CurrencyStatisticsDTO> getRecentResults() {

        return Arrays.stream(Optional.ofNullable(restTemplate
                        .getForObject("http://localhost:8081/api", CurrencyStatisticsDTO[].class))
                .orElse(new CurrencyStatisticsDTO[0])).toList();
    }

    public List<CurrencyStatisticsDTO> getOneResultByCode(String currency) {

        return Arrays.stream(Optional.ofNullable(restTemplate
                        .getForObject("http://localhost:8081/api/currency/{currency}", CurrencyStatisticsDTO[].class, currency))
                .orElse(new CurrencyStatisticsDTO[0])).toList();

    }

    public List<CurrencyStatisticsDTO> getOneResultByMonthAndYear(int month, int year) {

        return Arrays.stream(Optional.ofNullable(restTemplate
                        .getForObject("http://localhost:8081/api/history/{month}/{year}", CurrencyStatisticsDTO[].class, month, year))
                .orElse(new CurrencyStatisticsDTO[0])).toList();

    }

    public List<CurrencyStatisticsDTO> getOneResultByCodeAndMonthAndYear(int month, int year, String currency) {

        return Arrays.stream(Optional.ofNullable(restTemplate
                        .getForObject("http://localhost:8081/api/history/{month}/{year}/{currency}", CurrencyStatisticsDTO[].class, month, year, currency))
                .orElse(new CurrencyStatisticsDTO[0])).toList();

    }

    public List<CurrencyStatisticsDTO> increaseCount(List<String> incrementedCurrencies) {

        return Arrays.stream(Optional.ofNullable(restTemplate.
                postForObject("http://localhost:8081/api/requested_currencies/", incrementedCurrencies, CurrencyStatisticsDTO[].class))
                .orElse(new CurrencyStatisticsDTO[0])).toList();
    }


}
//todo interactive chart showing stats
//todo looks nice and fits the rest of the app's visuals
