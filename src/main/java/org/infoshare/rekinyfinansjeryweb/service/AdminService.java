package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.form.TableSettings;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

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

    public void editTable(String no, TableSettings settings) {
        Optional<DailyExchangeRates> dailyTable = NBPApiManager.getInstance().findDailyTable(no);
        dailyTable.ifPresentOrElse(table -> {
            DailyExchangeRates dailyExchangeRates = dailyTable.get();
            dailyExchangeRates.setNo(settings.getNo());
            dailyExchangeRates.setEffectiveDate(settings.getEffectiveDate());
            dailyExchangeRates.setTradingDate(settings.getTradingDate());
            NBPApiManager.getInstance().saveCollection();
        }, () -> {
            //TODO Add logger
        });
    }
}
