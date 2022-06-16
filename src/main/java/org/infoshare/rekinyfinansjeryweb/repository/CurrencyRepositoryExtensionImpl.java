package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.ExchangeRate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CurrencyRepositoryExtensionImpl implements CurrencyRepositoryExtension{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Currency> findCurrenciesWithCodes(List<String> codes) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Currency> criteriaQuery = criteriaBuilder.createQuery(Currency.class);
        Root<Currency> root = criteriaQuery.from(Currency.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = codes.stream().map(code -> criteriaBuilder.equal(root.get("code"), code)).collect(Collectors.toList());
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
