package org.infoshare.rekinyfinansjeryweb.repository;


import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String>, CurrencyRepositoryExtension {
}
