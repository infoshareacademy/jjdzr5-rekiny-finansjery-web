package org.infoshare.rekinyfinansjeryweb.formData;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class DailyTableForm {
    private String table;
    @NotBlank(message = "validation.no.blank")
    @Pattern(regexp="^[a-zA-Z0-9/]+$", message = "validation.no.pattern")
    @Size(min = 3, max = 14, message = "validation.no.size")
    private String no;
    @NotNull(message = "validation.effectiveDate.blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "validation.effectiveDate.past")
    private LocalDate effectiveDate;
    @NotNull(message = "validation.tradingDate.blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "validation.tradingDate.past")
    private LocalDate tradingDate;
}
