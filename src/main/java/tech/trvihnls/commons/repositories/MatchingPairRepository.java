package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.MatchingPair;

@Repository
public interface MatchingPairRepository extends JpaRepository<MatchingPair, Long> {
}
