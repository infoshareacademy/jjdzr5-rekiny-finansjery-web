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

    @Override
    public Long countDatesByFilterSettings(FiltrationSettingsDTO filtrationSettings, List<UUID> searchedCurrencies) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ExchangeRate> root = criteriaQuery.from(ExchangeRate.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.addAll(getPredicationExchangeRatesTableInPeriod(filtrationSettings, criteriaBuilder, root));
        predicates.addAll(getPredicationExchangeRateByPrices(filtrationSettings, criteriaBuilder, root));
        if(searchedCurrencies.size()>0) {
            predicates.add(criteriaBuilder.in(root.get("currency").get("id")).value(searchedCurrencies));
        }

        CriteriaQuery<Long> query = criteriaQuery
                .select(criteriaBuilder.countDistinct(root.get("date")))
                .where(predicates.toArray(Predicate[]::new));
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }

    public List<LocalDate> findDatesFromPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, List<UUID> searchedCurrencies, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LocalDate> criteriaQuery = criteriaBuilder.createQuery(LocalDate.class);
        Root<ExchangeRate> root = criteriaQuery.from(ExchangeRate.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.addAll(getPredicationExchangeRatesTableInPeriod(filtrationSettings, criteriaBuilder, root));
        predicates.addAll(getPredicationExchangeRateByPrices(filtrationSettings, criteriaBuilder, root));
        if(searchedCurrencies.size()>0) {
            predicates.add(criteriaBuilder.in(root.get("currency").get("id")).value(searchedCurrencies));
        }

        CriteriaQuery<LocalDate> query = criteriaQuery
                .select(root.get("date"))
                .where(predicates.toArray(Predicate[]::new))
                .groupBy(root.get("date"))
                .orderBy(criteriaBuilder.desc(root.get("date")));
        TypedQuery<LocalDate> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    @Override
    public List<ExchangeRateCurrency> findPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, List<UUID> searchedCurrencies, List<LocalDate> dates) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExchangeRateCurrency> criteriaQuery = criteriaBuilder.createQuery(ExchangeRateCurrency.class);
        Root<ExchangeRate> root = criteriaQuery.from(ExchangeRate.class);
        Join<ExchangeRate, Currency> currencyJoin = root.join("currency", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.addAll(getPredicationExchangeRatesTableInPeriod(filtrationSettings, criteriaBuilder, root));
        predicates.addAll(getPredicationExchangeRateByPrices(filtrationSettings, criteriaBuilder, root));
        predicates.add(criteriaBuilder.in(root.get("date")).value(dates));
        if(searchedCurrencies.size()>0) {
            predicates.add(criteriaBuilder.in(root.get("currency").get("id")).value(searchedCurrencies));
        }

        CriteriaQuery<ExchangeRateCurrency> query = criteriaQuery.select(criteriaBuilder.construct(ExchangeRateCurrency.class,
                        root.get("id"), root.get("date"), root.get("askPrice"),
                        root.get("bidPrice"), currencyJoin.get("code"), currencyJoin.get("name"),
                        currencyJoin.get("category")))
                .where(predicates.toArray(Predicate[]::new)).orderBy(criteriaBuilder.desc(root.get("date")));

        TypedQuery<ExchangeRateCurrency> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private List<Predicate> getPredicationExchangeRatesTableInPeriod(FiltrationSettingsDTO filtrationSettings,
                                                                     CriteriaBuilder criteriaBuilder, Root<ExchangeRate> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filtrationSettings.getDateMin() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filtrationSettings.getDateMin()));
        if (filtrationSettings.getDateMax() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filtrationSettings.getDateMax()));
        return predicates;
    }

    private List<Predicate> getPredicationExchangeRateByPrices(FiltrationSettingsDTO filtrationSettings,
                                                               CriteriaBuilder criteriaBuilder, Root<ExchangeRate> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filtrationSettings.getAskPriceMin() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("askPrice"),
                    filtrationSettings.getAskPriceMin()));
        if (filtrationSettings.getAskPriceMax() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("askPrice"),
                    filtrationSettings.getAskPriceMax()));
        if (filtrationSettings.getBidPriceMin() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bidPrice"),
                    filtrationSettings.getBidPriceMin()));
        if (filtrationSettings.getBidPriceMax() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("bidPrice"),
                    filtrationSettings.getBidPriceMax()));
        return predicates;
    }
}
