package org.infoshare.rekinyfinansjeryweb.hibernate;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Length(min = 3, max = 25)
    String code;

    @NotNull
    String name;

    @NotNull
    Float bidPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return Objects.equals(id, crypto.id) && Objects.equals(code, crypto.code) && Objects.equals(name, crypto.name) && Objects.equals(bidPrice, crypto.bidPrice) && Objects.equals(askingPrice, crypto.askingPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, bidPrice, askingPrice);
    }

    @NotNull
    Float askingPrice;
}