package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.NBPApiManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminPanelService {

    public List<DailyExchangeRates> getDailyExchangeRates() {
        NBPApiManager nbpApiManager = NBPApiManager.getInstance();
        return nbpApiManager.getCollectionsOfExchangeRates();
    }

}
