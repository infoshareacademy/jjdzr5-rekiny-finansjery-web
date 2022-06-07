package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTableExchangeRateCurrency;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRatesTable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.ArrayUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ExchangeRatesTableRepositoryExtensionImpl implements ExchangeRatesTableRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExchangeRatesTableExchangeRateCurrency> findExchangeRatesTableByFilterSettings(FiltrationSettingsDTO filtrationSettings) {

        //SELECT * FROM EXCHANGE_RATE RIGHT JOIN EXCHANGE_RATES_TABLE ON EXCHANGE_RATE.DAILY_TABLE = EXCHANGE_RATES_TABLE.ID

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExchangeRatesTableExchangeRateCurrency> cr = cb.createQuery(ExchangeRatesTableExchangeRateCurrency.class);
        Root<ExchangeRatesTable> root = cr.from(ExchangeRatesTable.class);
        Join<ExchangeRatesTable, ExchangeRate> joinExchangeRate = root.join("rates", JoinType.LEFT);
        Join<ExchangeRate, Currency> joinCurrencies = joinExchangeRate.join("currency", JoinType.LEFT);

        List<Predicate> predicates = getPredicationExchangeRatesTableFromFiltrationSettingsDTO(filtrationSettings, cb, root);
        predicates.addAll(getPredicationExchangeRateFromFiltrationSettingsDTO(filtrationSettings, cb, joinExchangeRate));
        predicates.addAll(getPredicationCurrenciesFromFiltrationSettingsDTO(filtrationSettings, cb, joinCurrencies));

        cr.select(cb.construct(ExchangeRatesTableExchangeRateCurrency.class,
                root.get("id"), root.get("no"), root.get("effectiveDate"), joinExchangeRate.get("askPrice"),
                joinExchangeRate.get("bidPrice"), joinCurrencies.get("code"), joinCurrencies.get("name"),
                joinCurrencies.get("category")))
                .where(predicates.toArray(Predicate[]::new));

        List<ExchangeRatesTableExchangeRateCurrency> results = entityManager.createQuery(cr).getResultList();

        results.forEach(rate -> {
            System.out.println(rate.getNo());
            System.out.println(rate.getAskPrice());
            System.out.println(rate.getBidPrice());
            System.out.println(rate.getCode());
            System.out.println(rate.getCategory());
            System.out.println(rate.getEffectiveDate());
            System.out.println(rate.getName());
            System.out.println("===================================================================");
        });
        return null;
    }

    private List<Predicate> getPredicationExchangeRatesTableFromFiltrationSettingsDTO(FiltrationSettingsDTO filtrationSettings, CriteriaBuilder cb,
        Root<ExchangeRatesTable> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filtrationSettings.getEffectiveDateMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveDate"), filtrationSettings.getEffectiveDateMin()));
        if (filtrationSettings.getEffectiveDateMax() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("effectiveDate"), filtrationSettings.getEffectiveDateMax()));
        if (filtrationSettings.getTradingDateMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("tradingDate"), filtrationSettings.getTradingDateMin()));
        if (filtrationSettings.getTradingDateMax() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("tradingDate"), filtrationSettings.getTradingDateMax()));
        return predicates;
    }
    private List<Predicate> getPredicationExchangeRateFromFiltrationSettingsDTO(FiltrationSettingsDTO filtrationSettings, CriteriaBuilder cb,
                                                                            Join<ExchangeRatesTable, ExchangeRate> joinExchangeRate) {
        List<Predicate> predicates = new ArrayList<>();
        if (filtrationSettings.getAskPriceMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(joinExchangeRate.get("askPrice"), filtrationSettings.getAskPriceMin()));
        if (filtrationSettings.getAskPriceMax() != null)
            predicates.add(cb.lessThanOrEqualTo(joinExchangeRate.get("askPrice"), filtrationSettings.getAskPriceMax()));
        if (filtrationSettings.getBidPriceMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(joinExchangeRate.get("bidPrice"), filtrationSettings.getBidPriceMin()));
        if (filtrationSettings.getBidPriceMax() != null)
            predicates.add(cb.lessThanOrEqualTo(joinExchangeRate.get("bidPrice"), filtrationSettings.getBidPriceMax()));
        return predicates;
    }
    private List<Predicate> getPredicationCurrenciesFromFiltrationSettingsDTO(FiltrationSettingsDTO filtrationSettings, CriteriaBuilder cb,
                                                                         Join<ExchangeRate, Currency> joinCurrencies){
        List<Predicate> predicates = new ArrayList<>();
        if(filtrationSettings.getCurrency()!=null && filtrationSettings.getCurrency().size()>0)
            predicates.add(getSelectedCurrencyPredicate(cb, joinCurrencies, filtrationSettings.getCurrency()));
        return predicates;
    }

    private Predicate getSelectedCurrencyPredicate(CriteriaBuilder cb, Join<ExchangeRate, Currency> joinCurrencies, List<String> currencies){
        List<Predicate> currenciesPredicates = new ArrayList<>();
        for(String currency : currencies){
            Predicate singleCurrencyPredicate = cb.equal(joinCurrencies.get("code"), currency);
            currenciesPredicates.add(singleCurrencyPredicate);
        }
        return cb.or(currenciesPredicates.toArray(new Predicate[0]));
    }
}
