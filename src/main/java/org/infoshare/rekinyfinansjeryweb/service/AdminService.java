package org.infoshare.rekinyfinansjeryweb.service;

import com.infoshareacademy.domain.DailyExchangeRates;
import com.infoshareacademy.domain.ExchangeRate;
import com.infoshareacademy.services.NBPApiManager;
import org.infoshare.rekinyfinansjeryweb.data.CurrencyData;
import org.infoshare.rekinyfinansjeryweb.data.DailyTableData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    public void addTable(DailyTableData dailyTableData) {
        DailyExchangeRates newDailyTable = new DailyExchangeRates();
        newDailyTable.setTable("C");
        newDailyTable.setNo(dailyTableData.getNo());
        newDailyTable.setEffectiveDate(dailyTableData.getEffectiveDate());
        newDailyTable.setTradingDate(dailyTableData.getTradingDate());

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

    public void editTable(String no, DailyTableData dailyTableData) {
        Optional<DailyExchangeRates> dailyTable = NBPApiManager.getInstance().findDailyTable(no);
        dailyTable.ifPresentOrElse(table -> {
            DailyExchangeRates dailyExchangeRates = dailyTable.get();
            dailyExchangeRates.setNo(dailyTableData.getNo());
            dailyExchangeRates.setEffectiveDate(dailyTableData.getEffectiveDate());
            dailyExchangeRates.setTradingDate(dailyTableData.getTradingDate());
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        }, () -> {
            //TODO Add logger
        });
    }

    public void addCurrency(String no, CurrencyData currencyData) {
        ExchangeRate newExchangeRate = new ExchangeRate();
        newExchangeRate.setCurrency(currencyData.getCurrency());
        newExchangeRate.setCode(currencyData.getCode());
        newExchangeRate.setBid(currencyData.getBid());
        newExchangeRate.setAsk(currencyData.getAsk());

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

    public void editCurrency(String tableNo, String code, CurrencyData currencyData) {
        Optional<ExchangeRate> exchangeRate = NBPApiManager.getInstance().findExchangeRate(tableNo, code);
        exchangeRate.ifPresentOrElse(rate -> {
            ExchangeRate newExchangeRate = exchangeRate.get();
            newExchangeRate.setCurrency(currencyData.getCurrency());
            newExchangeRate.setCode(currencyData.getCode());
            newExchangeRate.setBid(currencyData.getBid());
            newExchangeRate.setAsk(currencyData.getAsk());
            NBPApiManager.getInstance().saveCollection();
            //TODO Add logger
        }, () -> {
            //TODO Add logger
        });
    }

    public boolean tableExists(String tableNo) {
        return NBPApiManager.getInstance().findDailyTable(tableNo).isPresent();
    }

    public boolean currencyExists(String tableNo, String code) {
        Optional<ExchangeRate> exchangeRate = NBPApiManager.getInstance().findExchangeRate(tableNo, code);
        return exchangeRate.isPresent();
    }
}
