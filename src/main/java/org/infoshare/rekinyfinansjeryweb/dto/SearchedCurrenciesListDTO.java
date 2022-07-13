package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchedCurrenciesListDTO {
    private List<String> searchedCurrenciesCodes;
}
