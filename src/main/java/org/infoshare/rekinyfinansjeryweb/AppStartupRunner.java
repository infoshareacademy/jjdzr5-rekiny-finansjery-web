package org.infoshare.rekinyfinansjeryweb;

import org.infoshare.rekinyfinansjeryweb.service.CreateUserService;
import org.infoshare.rekinyfinansjeryweb.service.extrernalApi.ExternalDataApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    @Autowired
    ExternalDataApiService externalDataApiService;
    @Autowired
    CreateUserService createUserService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        externalDataApiService.getData();
        createUserService.createUsers();
    }
}