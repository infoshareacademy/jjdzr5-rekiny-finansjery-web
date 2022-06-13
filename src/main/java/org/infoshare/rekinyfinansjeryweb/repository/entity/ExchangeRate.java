package org.infoshare.rekinyfinansjeryweb.repository.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = ExchangeRate.TABLE_NAME)
public class ExchangeRate {
    public static final String TABLE_NAME = "exchange_rate";
    public static final String COLUMN_PREFIX = "er_";
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = COLUMN_PREFIX + "currency")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = COLUMN_PREFIX + "daily_table")
    private ExchangeRatesTable dailyTable;
    private Double askPrice;
    private Double bidPrice;
}
