package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.UserSubscription;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    @Query("select (count(u) > 0) from UserSubscription u where u.isActive = ?1 and u.paymentStatus = ?2")
    boolean existsByIsActiveAndPaymentStatus(boolean isActive, String paymentStatus);
}
