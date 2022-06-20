package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {

    @Query(value = "SELECT c FROM Currency c WHERE c.code = :code")
    Currency findByCode(@Param("code") String code);
}
