package org.infoshare.rekinyfinansjeryweb.repository;

import com.infoshareacademy.domain.DailyExchangeRates;
import org.infoshare.rekinyfinansjeryweb.entity.*;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.ArrayUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;

@Repository
public class ExchangeRatesTableRepositoryExtensionImpl implements ExchangeRatesTableRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ExchangeRatesTable> findExchangeRatesTableByFilterSettings(FiltrationSettingsDTO filtrationSettings) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExchangeRatesTable> cr = cb.createQuery(ExchangeRatesTable.class);
        Root<ExchangeRatesTable> root = cr.from(ExchangeRatesTable.class);
        List<Predicate> predicate = getPredicationExchangeRatesTableFromFiltrationSettingsDTO(filtrationSettings, cb, root);
        cr.select(root).where(predicate.toArray(Predicate[]::new)).orderBy(cb.desc(root.get("effectiveDate"))).groupBy(root.get("no"));
        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public List<ExchangeRatesTableExchangeRateCurrency> findExchangeRatesTableJoinExchangeRateJoinCurrencyByFilterSettings(List<String> tables, FiltrationSettingsDTO filtrationSettings, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExchangeRatesTableExchangeRateCurrency> cr = cb.createQuery(ExchangeRatesTableExchangeRateCurrency.class);
        Root<ExchangeRatesTable> root = cr.from(ExchangeRatesTable.class);
        Join<ExchangeRatesTable, ExchangeRate> joinExchangeRate = root.join("rates", JoinType.LEFT);
        Join<ExchangeRate, Currency> joinCurrencies = joinExchangeRate.join("currency", JoinType.LEFT);

        List<Predicate> predicates = getPredicationExchangeRateFromFiltrationSettingsDTO(filtrationSettings, cb, joinExchangeRate);
        predicates.addAll(getPredicationCurrenciesFromFiltrationSettingsDTO(filtrationSettings, cb, joinCurrencies));
        predicates.add(root.get("no").in(tables));

        CriteriaQuery<ExchangeRatesTableExchangeRateCurrency> criteriaQuery = cr.select(cb.construct(ExchangeRatesTableExchangeRateCurrency.class,
                        root.get("id"), root.get("no"), root.get("effectiveDate"), root.get("tradingDate"), joinExchangeRate.get("askPrice"),
                        joinExchangeRate.get("bidPrice"), joinCurrencies.get("code"), joinCurrencies.get("name"),
                        joinCurrencies.get("category")))
                .where(predicates.toArray(Predicate[]::new)).orderBy(cb.desc(root.get("effectiveDate")));

        TypedQuery<ExchangeRatesTableExchangeRateCurrency> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
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
