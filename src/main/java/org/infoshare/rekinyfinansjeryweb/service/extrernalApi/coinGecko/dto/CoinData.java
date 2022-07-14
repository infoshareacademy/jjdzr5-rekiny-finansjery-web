package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CoinData {
    private String id;
    private String symbol;
    private String name;
    @JsonProperty("market_data")
    private MarketData marketData;

    private LocalDate date;
}
