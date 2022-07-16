package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PossibleCurrency{
    private String code;
    private String category;
    private boolean checked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PossibleCurrency that = (PossibleCurrency) o;
        return checked == that.checked && Objects.equals(code, that.code) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, category, checked);
    }
}
