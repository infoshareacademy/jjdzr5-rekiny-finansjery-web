package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infoshare.rekinyfinansjeryweb.service.UsedCurrenciesService;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyTypeBucket{
    private String category;
    private List<PossibleCurrency> possibleCurrencies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyTypeBucket that = (CurrencyTypeBucket) o;
        return Objects.equals(category, that.category) && Objects.equals(possibleCurrencies, that.possibleCurrencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, possibleCurrencies);
    }
}
