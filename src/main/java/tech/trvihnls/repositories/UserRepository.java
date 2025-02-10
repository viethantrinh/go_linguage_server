package tech.trvihnls.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
