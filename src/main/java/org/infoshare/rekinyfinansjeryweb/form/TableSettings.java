package org.infoshare.rekinyfinansjeryweb.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TableSettings {
    private String table;
    private String no;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradingDate;
}
