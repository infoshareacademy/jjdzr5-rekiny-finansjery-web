package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MarketData {
    @JsonProperty("current_price")
    private CurrentPrice currentPrice;
}
