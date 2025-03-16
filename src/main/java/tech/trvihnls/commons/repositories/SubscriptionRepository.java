package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
