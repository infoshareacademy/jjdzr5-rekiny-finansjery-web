package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BTCCoinGeckoAdapter extends CoinGeckoAdapter{

    public BTCCoinGeckoAdapter(RestTemplate restTemplate) {
        super(restTemplate, "bitcoin", "COIN_GECKO_API_BITCOIN");
    }
}
