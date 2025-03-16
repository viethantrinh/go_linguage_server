package tech.trvihnls.commons.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_invalidated_token")
public class InvalidatedToken {
    @Id
    @Column(name = "jwt_id")
    private String jwtId;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InvalidatedToken that = (InvalidatedToken) o;
        return Objects.equals(jwtId, that.jwtId) && Objects.equals(expirationTime, that.expirationTime);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(jwtId);
        result = 31 * result + Objects.hashCode(expirationTime);
        return result;
    }
}
