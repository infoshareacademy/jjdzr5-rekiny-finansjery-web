package org.infoshare.rekinyfinansjeryweb.formData;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ExchangeRateForm {
    @NotBlank(message = "validation.currency.blank")
    @Size(min = 3, max = 20, message = "validation.currency.size")
    private String currency;
    @NotBlank(message = "validation.code.blank")
    @Size(min = 3, max = 3, message = "validation.code.size")
    @Pattern(regexp="^[a-zA-Z]+$", message = "validation.code.pattern")
    private String code;
    @Min(value = 0, message = "validation.bid.size")
    @Max(value = 100, message = "validation.bid.size")
    private double bid;
    @Min(value = 0, message = "validation.ask.size")
    @Max(value = 100, message = "validation.ask.size")
    private double ask;
}
