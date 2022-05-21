package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.formData.TableSettings;
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
            //TODO Add logger
        }, () -> {
            //TODO Add logger
        });
    }

    public void addCurrency(String no, ExchangeRate newExchangeRate) {
        if (NBPApiManager.getInstance().addExchangeRate(no, newExchangeRate)) {
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        } else {
            //TODO Add logger
        }
    }

    public void deleteCurrency(String tableNo, String code) {
        if (NBPApiManager.getInstance().removeExchangeRate(tableNo, code)) {
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        } else {
            //TODO Add logger
        }
    }

    public void editCurrency(String tableNo, String code, ExchangeRate editedRate) {
        Optional<ExchangeRate> exchangeRate = NBPApiManager.getInstance().findExchangeRate(tableNo, code);
        exchangeRate.ifPresentOrElse(rate -> {
            ExchangeRate newExchangeRate = exchangeRate.get();
            newExchangeRate.setCurrency(editedRate.getCurrency());
            newExchangeRate.setCode(editedRate.getCode());
            newExchangeRate.setBid(editedRate.getBid());
            newExchangeRate.setAsk(editedRate.getAsk());
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        }, () -> {
            //TODO Add logger
        });
    }
}
