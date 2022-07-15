package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.Getter;
import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.dto.SearchSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyRepositoryExtensionImpl implements CurrencyRepositoryExtension{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Currency> findAllCurrencyByCodeIn(FiltrationSettingsDTO filtrationSettings) {
        CurrencyQuery<Currency> query = new CurrencyQuery<>(Currency.class);

        CriteriaQuery<Currency> criteriaQuery = query.getCriteriaQuery()
                .select(query.getRoot());
        if(!filtrationSettings.getCurrency().isEmpty()){
            criteriaQuery.where(query.getCriteriaBuilder().in(query.getRoot().get("code")).value(filtrationSettings.getCurrency()));
        }
        TypedQuery<Currency> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public List<Currency> findAllCurrencyByCodeIn(SearchSettingsDTO searchSettings) {
        CurrencyQuery<Currency> query = new CurrencyQuery<>(Currency.class);

        Predicate predicate = createPredicateForSearchSettings(searchSettings, query);

        CriteriaQuery<Currency> criteriaQuery = query.getCriteriaQuery()
                .select(query.getRoot()).where(predicate);
        TypedQuery<Currency> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();

    }

    private Predicate createPredicateForSearchSettings(SearchSettingsDTO searchSettings, CurrencyQuery query){
        if(!searchSettings.getCurrency().isEmpty()) {
            return query.getCriteriaBuilder()
                    .and(query.getCriteriaBuilder().in(query.getRoot().get("code")).value(searchSettings.getCurrency()),
                            createPredicateForSearchedPhrases(searchSettings, query));
        }
        else{
            return query.getCriteriaBuilder().and(createPredicateForSearchedPhrases(searchSettings, query));
        }
    }

    private Predicate createPredicateForSearchedPhrases(SearchSettingsDTO searchSettings, CurrencyQuery query){
        List<Predicate> searchedPhrases = new ArrayList<>();
        List<String> phrases = Arrays.stream(searchSettings.getSearchPhrase().split(" ")).map(str -> "%"+str+"%").toList();
        phrases.forEach(phrase ->{
            searchedPhrases.add(query.getCriteriaBuilder().like(query.getRoot().get("code"), phrase));
            searchedPhrases.add(query.getCriteriaBuilder().like(query.getRoot().get("name"), phrase));
            searchedPhrases.add(query.getCriteriaBuilder().like(query.getRoot().get("tags"), phrase));
        });
        return query.getCriteriaBuilder().or(searchedPhrases.toArray(Predicate[]::new));
    }

    @Getter
    private class CurrencyQuery<T>{
        private final CriteriaBuilder criteriaBuilder;
        private final CriteriaQuery<T> criteriaQuery;
        private final Root<Currency> root;

        CurrencyQuery(Class<T> tClass){
            criteriaBuilder = entityManager.getCriteriaBuilder();
            criteriaQuery = criteriaBuilder.createQuery(tClass);
            root = criteriaQuery.from(Currency.class);
        }
    }

}
