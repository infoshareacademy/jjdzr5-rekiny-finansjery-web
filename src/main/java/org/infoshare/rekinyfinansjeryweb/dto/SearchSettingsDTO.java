package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.Data;

@Data
public class SearchSettingsDTO extends FiltrationSettingsDTO {
    public SearchSettingsDTO(){
        super();
    }
    private String searchPhrase;
}