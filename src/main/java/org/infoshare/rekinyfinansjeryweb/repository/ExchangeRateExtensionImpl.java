package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.Getter;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
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
    public Long countDatesBySearchSettings(SearchSettingsDTO searchSettings) {
        ExchangeRateQuery<Long> query = new ExchangeRateQuery<>(Long.class);
        Subquery<String> sub = createSubqueryForCurrenciesFromSearchSettings(searchSettings, query);

        List<Predicate> predicates = createCommonPredicatesForFiltrationSettings(searchSettings, query, sub);

        return buildAndExecuteCountDatesQuery(query, predicates);
    }

    @Override
    public Long countDatesByFilterSettings(FiltrationSettingsDTO filtrationSettings) {
        ExchangeRateQuery<Long> query = new ExchangeRateQuery<>(Long.class);
        Subquery<String> sub = createSubqueryForCurrenciesFromFiltrationSettings(filtrationSettings, query);

        List<Predicate> predicates = createCommonPredicatesForFiltrationSettings(filtrationSettings, query, sub);

        return buildAndExecuteCountDatesQuery(query, predicates);
    }

    private Long buildAndExecuteCountDatesQuery(ExchangeRateQuery<Long> query, List<Predicate> predicates){
        CriteriaQuery<Long> criteriaQuery = query.getCriteriaQuery()
                .select(query.getCriteriaBuilder().countDistinct(query.getRoot().get("date")))
                .where(predicates.toArray(Predicate[]::new));
        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<LocalDate> findDatesFromPageByFilterSettings(FiltrationSettingsDTO filtrationSettings, Pageable pageable){
        ExchangeRateQuery<LocalDate> query = new ExchangeRateQuery<>(LocalDate.class);
        Subquery<String> sub = createSubqueryForCurrenciesFromFiltrationSettings(filtrationSettings, query);

        List<Predicate> predicates = createCommonPredicatesForFiltrationSettings(filtrationSettings, query, sub);

        return  buildAndExecuteFindDatesFromPageQuery(query, predicates, pageable);
    }

    @Override
    public List<LocalDate> findDatesFromPageBySearchSettings(SearchSettingsDTO searchSettings, Pageable pageable){
        ExchangeRateQuery<LocalDate> query = new ExchangeRateQuery<>(LocalDate.class);
        Subquery<String> sub = createSubqueryForCurrenciesFromSearchSettings(searchSettings, query);

        List<Predicate> predicates = createCommonPredicatesForFiltrationSettings(searchSettings, query, sub);

        return  buildAndExecuteFindDatesFromPageQuery(query, predicates, pageable);
    }

    private List<LocalDate> buildAndExecuteFindDatesFromPageQuery(ExchangeRateQuery<LocalDate> query, List<Predicate> predicates, Pageable pageable){
        CriteriaQuery<LocalDate> criteriaQuery = query.getCriteriaQuery()
                .select(query.getRoot().get("date"))
                .where(predicates.toArray(Predicate[]::new))
                .groupBy(query.getRoot().get("date"))
                .orderBy(query.getCriteriaBuilder().desc(query.getRoot().get("date")));
        TypedQuery<LocalDate> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    @Override
    public List<ExchangeRateCurrency> findPageBySearchSettings(FiltrationSettingsDTO filtrationSettings, List<LocalDate> dates) {
        ExchangeRateQuery<ExchangeRateCurrency> query = new ExchangeRateQuery<>(ExchangeRateCurrency.class);
        Join<ExchangeRate, Currency> currencyJoin = query.getRoot().join("currency", JoinType.LEFT);
        Subquery<String> sub = createSubqueryForCurrenciesFromFiltrationSettings(filtrationSettings, query);

        List<Predicate> predicates = createCommonPredicatesForFiltrationSettings(filtrationSettings, query, sub);
        predicates.add(query.getCriteriaBuilder().in(query.getRoot().get("date")).value(dates));

        return buildAndExecuteFindPageQuery(query, currencyJoin, predicates);
    }

    @Override
    public List<ExchangeRateCurrency> findPageBySearchSettings(SearchSettingsDTO searchSettings, List<LocalDate> dates) {
        ExchangeRateQuery<ExchangeRateCurrency> query = new ExchangeRateQuery<>(ExchangeRateCurrency.class);
        Join<ExchangeRate, Currency> currencyJoin = query.getRoot().join("currency", JoinType.LEFT);
        Subquery<String> sub = createSubqueryForCurrenciesFromSearchSettings(searchSettings, query);

        List<Predicate> predicates = createCommonPredicatesForFiltrationSettings(searchSettings, query, sub);
        predicates.add(query.getCriteriaBuilder().in(query.getRoot().get("date")).value(dates));

        return buildAndExecuteFindPageQuery(query, currencyJoin, predicates);
    }

    private List<ExchangeRateCurrency> buildAndExecuteFindPageQuery(ExchangeRateQuery<ExchangeRateCurrency> query,
                                                            Join<ExchangeRate, Currency> currencyJoin, List<Predicate> predicates){
        CriteriaQuery<ExchangeRateCurrency> criteriaQuery = query.getCriteriaQuery()
                .select(query.getCriteriaBuilder().construct(ExchangeRateCurrency.class,
                        query.getRoot().get("id"), query.getRoot().get("date"), query.getRoot().get("askPrice"),
                        query.getRoot().get("bidPrice"), currencyJoin.get("code"), currencyJoin.get("name"),
                        currencyJoin.get("category")))
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(query.getCriteriaBuilder().desc(query.getRoot().get("date")));

        TypedQuery<ExchangeRateCurrency> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public List<LastRating> findLastRatingDateForEachCurrency() {
        return null;
    }

    private List<Predicate> createCommonPredicatesForFiltrationSettings(FiltrationSettingsDTO filtrationSettings, ExchangeRateQuery query, Subquery<String> sub){
        List<Predicate> predicates = new ArrayList<>();
        predicates.addAll(getPredicationExchangeRatesTableInPeriod(filtrationSettings, query.getCriteriaBuilder(), query.getRoot()));
        predicates.addAll(getPredicationExchangeRateByPrices(filtrationSettings, query.getCriteriaBuilder(), query.getRoot()));
        predicates.add(query.getCriteriaBuilder().in(query.getRoot().get("currency").get("code")).value(sub));
        return predicates;
    }

    private Subquery<String> createSubqueryForCurrenciesFromFiltrationSettings(FiltrationSettingsDTO filtrationSettings, ExchangeRateQuery query){
        Subquery<String> sub = query.getCriteriaQuery().subquery(String.class);
        Root<Currency> subRoot = sub.from(Currency.class);
        sub.select(subRoot.get("code")).where(query.getCriteriaBuilder().in(subRoot.get("code")).value(filtrationSettings.getCurrency()));
        return sub;
    }

    private Subquery<String> createSubqueryForCurrenciesFromSearchSettings(SearchSettingsDTO searchSettings, ExchangeRateQuery query){
        Subquery<String> sub = query.getCriteriaQuery().subquery(String.class);
        Root<Currency> subRoot = sub.from(Currency.class);
        List<Predicate> predicates = new ArrayList<>();
        if(!searchSettings.getCurrency().isEmpty()) {
            predicates.add(query.getCriteriaBuilder().in(subRoot.get("code")).value(searchSettings.getCurrency()));
        }

        predicates.add(createPredicateForSearchedPhrases(searchSettings, query, subRoot));

        sub.select(subRoot.get("code")).where(predicates.toArray(Predicate[]::new));
        return sub;
    }

    private Predicate createPredicateForSearchedPhrases(SearchSettingsDTO searchSettings, ExchangeRateQuery query, Root<Currency> subRoot){
        List<Predicate> searchedPhrases = new ArrayList<>();
        List<String> phrases = Arrays.stream(searchSettings.getSearchPhrase().split(" ")).map(str -> "%"+str+"%").toList();
        phrases.forEach(phrase ->{
            searchedPhrases.add(query.getCriteriaBuilder().or(
                    query.getCriteriaBuilder().like(subRoot.get("code"), phrase),
                    query.getCriteriaBuilder().like(subRoot.get("name"), phrase)));
        });
        return query.getCriteriaBuilder().or(searchedPhrases.toArray(Predicate[]::new));
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

    @Getter
    private class ExchangeRateQuery<T>{
        private final CriteriaBuilder criteriaBuilder;
        private final CriteriaQuery<T> criteriaQuery;
        private final Root<ExchangeRate> root;
        ExchangeRateQuery(Class<T> tClass){
            criteriaBuilder = entityManager.getCriteriaBuilder();
            criteriaQuery = criteriaBuilder.createQuery(tClass);
            root = criteriaQuery.from(ExchangeRate.class);
        }
    }
}
