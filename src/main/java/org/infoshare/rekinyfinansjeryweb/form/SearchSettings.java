package org.infoshare.rekinyfinansjeryweb.form;

import lombok.Data;
import org.stringtemplate.v4.ST;

import java.util.HashMap;
import java.util.Map;

@Data
public class SearchSettings extends FiltrationSettings {
    public SearchSettings(){
        super();
    }
    private String searchPhrase;
    private String searchType;
}