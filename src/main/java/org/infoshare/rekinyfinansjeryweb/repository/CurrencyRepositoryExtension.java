package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;

import java.util.List;

public interface CurrencyRepositoryExtension {
    List<Currency> findCurrenciesWithCodes(List<String> codes);
}
