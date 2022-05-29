package org.infoshare.rekinyfinansjeryweb.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    Long id;
    @NotNull
    @Min(1)
    @Max(3)
    String code;
    @NotNull
    @Max(25)
    String name;
    @NotNull
    BigDecimal bidPrice;
    @NotNull
    BigDecimal askingPrice;
}
