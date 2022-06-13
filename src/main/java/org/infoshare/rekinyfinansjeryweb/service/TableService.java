package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.repository.ExchangeRatesTableRepository;
import org.infoshare.rekinyfinansjeryweb.repository.entity.ExchangeRatesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TableService {

    @Autowired
    ExchangeRatesTableRepository exchangeRatesTableRepository;

    public Optional<ExchangeRatesTable> findTable(String tableNo) {
        return Optional.of(exchangeRatesTableRepository.findByNo(tableNo));
    }
}
