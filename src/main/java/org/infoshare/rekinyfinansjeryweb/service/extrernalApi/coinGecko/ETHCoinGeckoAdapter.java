package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ETHCoinGeckoAdapter extends CoinGeckoAdapter{
    public ETHCoinGeckoAdapter(RestTemplate restTemplate) {
        super(restTemplate, "ethereum", "COIN_GECKO_API_ETHEREUM");
    }
}
