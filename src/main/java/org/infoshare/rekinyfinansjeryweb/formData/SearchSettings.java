package org.infoshare.rekinyfinansjeryweb.formData;

import lombok.Data;

@Data
public class SearchSettings extends FiltrationSettings {
    public SearchSettings(){
        super();
    }
    private String searchPhrase;
    private String searchType;
}