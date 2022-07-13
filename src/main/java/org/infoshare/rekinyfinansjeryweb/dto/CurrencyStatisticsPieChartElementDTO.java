package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyStatisticsPieChartElementDTO {
    String code;
    Long counter;
}
