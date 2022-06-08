package org.infoshare.rekinyfinansjeryweb.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyTableDTO {
    String no;
    LocalDate effectiveDate;
    LocalDate tradingDate;
    List<ExchangeRateDTO> rates;
}
