package org.infoshare.rekinyfinansjeryweb.dto;

import javax.validation.constraints.NotNull;

public class PayMethodFormDTO extends AmountFormDTO {

    @NotNull(message = "{typeMismatch}")
    private String payMethod;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PayMethodFormDTO that = (PayMethodFormDTO) o;

        return payMethod != null ? payMethod.equals(that.payMethod) : that.payMethod == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (payMethod != null ? payMethod.hashCode() : 0);
        return result;
    }
}
