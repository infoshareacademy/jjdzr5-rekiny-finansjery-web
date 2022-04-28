package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.form.TableSettings;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {

    public void addTable(TableSettings settings) {
        DailyExchangeRates newDailyTable = new DailyExchangeRates();
        newDailyTable.setTable("C");
        newDailyTable.setNo(settings.getNo());
        newDailyTable.setEffectiveDate(settings.getEffectiveDate());
        newDailyTable.setTradingDate(settings.getTradingDate());

        if (NBPApiManager.getInstance().addDailyTable(newDailyTable)) {
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        } else {
            //TODO Add logger
        }
    }

    public void deleteTable(String tableNo) {
        if (NBPApiManager.getInstance().removeDailyTable(tableNo)) {
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        } else {
            //TODO Add logger
        }
    }

}
