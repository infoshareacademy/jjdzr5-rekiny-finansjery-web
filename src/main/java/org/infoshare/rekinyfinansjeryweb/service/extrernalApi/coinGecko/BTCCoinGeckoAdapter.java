package org.infoshare.rekinyfinansjeryweb.service.extrernalApi.coinGecko;

import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.CurrencyTagGenerator;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BTCCoinGeckoAdapter extends CoinGeckoAdapter{

    public BTCCoinGeckoAdapter(RestTemplate restTemplate, CurrencyTagGenerator currencyTagGenerator) {
        super(restTemplate, currencyTagGenerator,"bitcoin", "COIN_GECKO_API_BITCOIN");
    }
}
