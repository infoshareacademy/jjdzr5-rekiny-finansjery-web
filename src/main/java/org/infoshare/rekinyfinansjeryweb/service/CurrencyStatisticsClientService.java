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

@Service
public class CurrencyStatisticsClientService {

    @Autowired
    RestTemplate restTemplate;

    public List <CurrencyStatisticsDTO> getResults(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <CurrencyStatisticsDTO[]> entity = new HttpEntity<CurrencyStatisticsDTO[]>(headers);

        return Arrays.stream(restTemplate.exchange("http://localhost:8081/api", HttpMethod.GET, entity, CurrencyStatisticsDTO[].class).getBody()).toList();
    }

    public void increaseCount(List <String> incrementedCurrencies){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        SearchedCurrenciesListDTO searchedCurrenciesListDTO = new SearchedCurrenciesListDTO(incrementedCurrencies);
        HttpEntity<SearchedCurrenciesListDTO> entity = new HttpEntity<SearchedCurrenciesListDTO>(searchedCurrenciesListDTO,headers);

        restTemplate.exchange(
                "http://localhost:8081/api/increment", HttpMethod.POST, entity, SearchedCurrenciesListDTO.class).getBody();
    }
}
