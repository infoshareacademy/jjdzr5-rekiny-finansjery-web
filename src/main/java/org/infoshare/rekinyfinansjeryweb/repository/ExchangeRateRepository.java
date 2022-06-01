package org.infoshare.rekinyfinansjeryweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
