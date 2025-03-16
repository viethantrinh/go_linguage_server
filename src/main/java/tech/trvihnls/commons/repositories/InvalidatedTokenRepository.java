package tech.trvihnls.commons.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.InvalidatedToken;


@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsByJwtId(String jwtId);
}