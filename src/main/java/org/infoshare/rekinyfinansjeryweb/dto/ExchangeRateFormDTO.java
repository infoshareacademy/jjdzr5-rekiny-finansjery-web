package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ExchangeRateFormDTO {
    @NotNull(message = "validation.date.blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "validation.date.past")
    private LocalDate date;

    @NotBlank(message = "validation.currency.blank")
    @Size(min = 3, max = 20, message = "validation.currency.size")
    private String currency;

    @NotBlank(message = "validation.code.blank")
    @Size(min = 3, max = 3, message = "validation.code.size")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "validation.code.pattern")
    private String code;

    @NotBlank(message = "validation.category.blank")
    @Size(min = 3, max = 20, message = "validation.category.size")
    private String category;

    @Min(value = 0, message = "validation.bid.size")
    private double bid;

    @Min(value = 0, message = "validation.ask.size")
    private double ask;
}
