package org.infoshare.rekinyfinansjeryweb.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    String code;
    @NotNull
    String name;
    @NotNull
    String category;
}
