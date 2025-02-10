package tech.trvihnls.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.models.Role;
import tech.trvihnls.models.User;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r FROM Role r INNER JOIN UserHasRole ur ON r.id = ur.user.id WHERE ur.user.id = :userId")
    List<Role> getAllByUserId(Long userId);
}
