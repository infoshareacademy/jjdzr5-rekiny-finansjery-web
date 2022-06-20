package org.infoshare.rekinyfinansjeryweb.repository;

import org.infoshare.rekinyfinansjeryweb.entity.LastUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LastUpdateRepository extends JpaRepository<LastUpdate, UUID> {
    Optional<LastUpdate> findBySourceName(String sourceName);
}
