package org.infoshare.rekinyfinansjeryweb.service.extrernalApi;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CurrencyTagGenerator {
    MessageSource messageSource;
    List<Locale> locales;


    public CurrencyTagGenerator(MessageSource messageSource) {
        this.messageSource = messageSource;
        locales = List.of(Locale.ENGLISH, new Locale("pl_PL"));
    }

    public String createTag(String code){
        String tag = locales.stream()
                .map(locale -> messageSource.getMessage("currency."+code, new Object[0], locale).toLowerCase(Locale.ROOT)+";")
                .reduce("", String::concat);
        return tag;
    }
}
