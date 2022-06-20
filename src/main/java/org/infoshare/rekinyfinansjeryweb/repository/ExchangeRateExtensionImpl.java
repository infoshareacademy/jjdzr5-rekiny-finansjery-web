package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.*;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;

@Repository
public class ExchangeRateExtensionImpl implements ExchangeRateExtension {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ExchangeRateCurrency> findExchangeRateJoinCurrencyByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExchangeRateCurrency> cq = cb.createQuery(ExchangeRateCurrency.class);
        Root<ExchangeRate> root = cq.from(ExchangeRate.class);
        Path<Object> path = root.get("date");

        Subquery<ExchangeRate> subquery = cq.subquery(ExchangeRate.class);
        Root<ExchangeRate> subQueryRoot = subquery.from(ExchangeRate.class);
        subquery.select(subQueryRoot.get("date"));
        subquery.where(getPredicationExchangeRatesTableFromFiltrationSettingsDTO(filtrationSettings, cb, subQueryRoot).toArray(Predicate[]::new));
        subquery.groupBy(subQueryRoot.get("date"));

        Join<ExchangeRate, Currency> joinCurrencies = root.join("currency", JoinType.LEFT);

        List<Predicate> predicates = getPredicationExchangeRateFromFiltrationSettingsDTO(filtrationSettings, cb, root);
        predicates.addAll(getPredicationCurrenciesFromFiltrationSettingsDTO(filtrationSettings, cb, joinCurrencies));
        predicates.add(cb.in(path).value(subquery));

        CriteriaQuery<ExchangeRateCurrency> criteriaQuery = cq.select(cb.construct(ExchangeRateCurrency.class,
                        root.get("id"), root.get("date"), root.get("askPrice"),
                        root.get("bidPrice"), joinCurrencies.get("code"), joinCurrencies.get("name"),
                        joinCurrencies.get("category")))
                .where(predicates.toArray(Predicate[]::new)).orderBy(cb.desc(root.get("date")));

        TypedQuery<ExchangeRateCurrency> typedQuery = entityManager.createQuery(criteriaQuery);
        //typedQuery.setFirstResult((int)pageable.getOffset());
        //typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    private List<Predicate> getPredicationExchangeRatesTableFromFiltrationSettingsDTO(FiltrationSettingsDTO filtrationSettings, CriteriaBuilder cb,
                                                                                      Root<ExchangeRate> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filtrationSettings.getEffectiveDateMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filtrationSettings.getEffectiveDateMin()));
        if (filtrationSettings.getEffectiveDateMax() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("date"), filtrationSettings.getEffectiveDateMax()));
        return predicates;
    }
    private List<Predicate> getPredicationExchangeRateFromFiltrationSettingsDTO(FiltrationSettingsDTO filtrationSettings, CriteriaBuilder cb,
            Root<ExchangeRate> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filtrationSettings.getAskPriceMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("askPrice"), filtrationSettings.getAskPriceMin()));
        if (filtrationSettings.getAskPriceMax() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("askPrice"), filtrationSettings.getAskPriceMax()));
        if (filtrationSettings.getBidPriceMin() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("bidPrice"), filtrationSettings.getBidPriceMin()));
        if (filtrationSettings.getBidPriceMax() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("bidPrice"), filtrationSettings.getBidPriceMax()));
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
