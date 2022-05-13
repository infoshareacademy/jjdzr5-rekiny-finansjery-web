package org.infoshare.rekinyfinansjeryweb.form;

import lombok.Data;

@Data
public class SaveOfFiltrationSettings extends FiltrationSettings {
    public SaveOfFiltrationSettings(){
        super();
    }
    private String preferenceName;

    @Override
    public String toString() {
        return "SaveOfFiltrationSettings{" +
                "preferenceName='" + preferenceName + '\'' +
                super.toString()+
                '}';
    }
}