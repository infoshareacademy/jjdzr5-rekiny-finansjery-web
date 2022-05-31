package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Currency {
    @Id
    String code;
    @NotNull
    String name;
    @NotNull
    String category;
}
