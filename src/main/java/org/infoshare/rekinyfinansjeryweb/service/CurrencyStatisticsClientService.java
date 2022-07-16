package org.infoshare.rekinyfinansjeryweb.service;

import lombok.extern.slf4j.Slf4j;
import org.infoshare.rekinyfinansjeryweb.dto.CurrencyStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class CurrencyStatisticsClientService {

    private static final String BASE_URL = "http://localhost:8081";
    @Autowired
    RestTemplate restTemplate;

    public List<CurrencyStatisticsDTO> getAllResults() {

        try {
            return Arrays.stream(Optional.ofNullable(restTemplate
                            .getForObject(BASE_URL + "/api/all", CurrencyStatisticsDTO[].class)) //TODO wyrzucić URL do stałej
                    .orElse(new CurrencyStatisticsDTO[0])).sorted(Comparator.comparing(CurrencyStatisticsDTO::getDate)).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }

    public List<CurrencyStatisticsDTO> getRecentResults() {

        try {
            return Arrays.stream(Optional.ofNullable(restTemplate
                            .getForObject(BASE_URL + "/api", CurrencyStatisticsDTO[].class))
                    .orElse(new CurrencyStatisticsDTO[0])).sorted(Comparator.comparing(CurrencyStatisticsDTO::getDate)).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }

    public List<CurrencyStatisticsDTO> getOneResultByCode(String currency) {

        try {
            return Arrays.stream(Optional.ofNullable(restTemplate
                            .getForObject(BASE_URL + "/api/currency/{currency}", CurrencyStatisticsDTO[].class, currency))
                    .orElse(new CurrencyStatisticsDTO[0])).sorted(Comparator.comparing(CurrencyStatisticsDTO::getDate)).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }

    public List<CurrencyStatisticsDTO> getOneResultByMonthAndYear(int month, int year) {

        try {
            return Arrays.stream(Optional.ofNullable(restTemplate
                            .getForObject(BASE_URL + "/api/history/{month}/{year}", CurrencyStatisticsDTO[].class, month, year))
                    .orElse(new CurrencyStatisticsDTO[0])).sorted(Comparator.comparing(CurrencyStatisticsDTO::getDate)).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }

    public List<CurrencyStatisticsDTO> getOneResultByCodeAndMonthAndYear(int month, int year, String currency) {

        try {
            return Arrays.stream(Optional.ofNullable(restTemplate
                            .getForObject(BASE_URL + "/api/history/{month}/{year}/{currency}", CurrencyStatisticsDTO[].class, month, year, currency))
                    .orElse(new CurrencyStatisticsDTO[0])).sorted(Comparator.comparing(CurrencyStatisticsDTO::getDate)).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }

    public List<CurrencyStatisticsDTO> increaseCount(List<String> incrementedCurrencies) {

        try {
            return Arrays.stream(Optional.ofNullable(restTemplate.
                            postForObject(BASE_URL + "/api/requested_currencies/", incrementedCurrencies, CurrencyStatisticsDTO[].class))
                    .orElse(new CurrencyStatisticsDTO[0])).sorted(Comparator.comparing(CurrencyStatisticsDTO::getDate)).toList();
        } catch (Exception e) {
            log.info("Problem with contacting the Stat service, returning empty List instead");
            return List.of();
        }
    }


}
