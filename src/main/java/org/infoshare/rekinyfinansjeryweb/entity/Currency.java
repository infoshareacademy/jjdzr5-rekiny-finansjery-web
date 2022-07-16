package org.infoshare.rekinyfinansjeryweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Currency.TABLE_NAME)
public class Currency {
    public static final String TABLE_NAME = "currency";
    public static final String COLUMN_PREFIX = "c_";
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;
    @Column(name = COLUMN_PREFIX + "code", unique = true)
    String code;
    @NotNull
    @Column(name = COLUMN_PREFIX + "name")
    String name;
    @NotNull
    @Column(name = COLUMN_PREFIX + "category")
    String category;
    @OneToMany(mappedBy = "currency")
    List<ExchangeRate> currencies;
    @Column(name = COLUMN_PREFIX + "tags", columnDefinition = "varchar(255) default ''")
    String tags;
}
