package org.infoshare.rekinyfinansjeryweb.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Beans {

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(3);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate(){ return new RestTemplate(); }
}
