package org.infoshare.rekinyfinansjeryweb.config;

import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExternalDataApiService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DataUpdater {
    ExternalDataApiService externalDataApiService;

    public DataUpdater(ExternalDataApiService externalDataApiService) {
        this.externalDataApiService = externalDataApiService;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void scheduleFixedRateTask() {
        externalDataApiService.getData();
    }
}
