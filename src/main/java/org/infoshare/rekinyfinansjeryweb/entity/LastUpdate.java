package org.infoshare.rekinyfinansjeryweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = LastUpdate.TABLE_NAME)
public class LastUpdate {
    public static final String TABLE_NAME = "last_update";
    public static final String COLUMN_PREFIX = "lu_";
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;

    @NotNull
    @Column(name = COLUMN_PREFIX + "source_name")
    private String sourceName;

    @NotNull
    @Column(name = COLUMN_PREFIX + "update_time")
    private LocalDateTime updateTime;
}
