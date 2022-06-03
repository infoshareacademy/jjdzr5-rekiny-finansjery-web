package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ExchangeRatesTableRepositoryImpl implements ExchangeRatesTableRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings(FiltrationSettings filtrationSettings) {

        //SELECT * FROM EXCHANGE_RATE RIGHT JOIN EXCHANGE_RATES_TABLE ON EXCHANGE_RATE.DAILY_TABLE = EXCHANGE_RATES_TABLE.ID

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExchangeRate> cr = cb.createQuery(ExchangeRate.class);
        Root<ExchangeRate> root = cr.from(ExchangeRate.class);
        Join<ExchangeRate, ExchangeRatesTable> joinTables = root.join("dailyTable");
        Join<ExchangeRate, Currency> joinCurrencies = root.join("currency", JoinType.LEFT);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.greaterThan(joinTables.get("effectiveDate"), LocalDate.now().minusDays(4));
        predicates[1] = getSelectedCurrencyPredicate(cb, joinCurrencies, Arrays.asList("USD", "JPY"));

        cr.select(root).where(predicates);
        List<ExchangeRate> results = entityManager.createQuery(cr).getResultList();



        results.forEach(rate -> {
            System.out.println(rate.getDailyTable().getNo());
            System.out.println(rate.getDailyTable().getRates().size());
            System.out.println(rate.getAskPrice());
            System.out.println(rate.getBidPrice());
            System.out.println(rate.getCurrency().getCode());
            System.out.println("===================================================================");
        });
        return null;
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
