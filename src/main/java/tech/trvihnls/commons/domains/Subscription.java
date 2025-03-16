package tech.trvihnls.commons.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_subscription")
public class Subscription extends BaseEntity {

    @Column(name = "name", length = 32, nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private int price;

    @Column(name = "stripe_price_id", length = 64, nullable = false, unique = true)
    private String stripePriceId; // this is an id of product (with the price) in stripe

    @Column(name = "duration_in_month", nullable = false, unique = true)
    private int durationInMonth;
}
