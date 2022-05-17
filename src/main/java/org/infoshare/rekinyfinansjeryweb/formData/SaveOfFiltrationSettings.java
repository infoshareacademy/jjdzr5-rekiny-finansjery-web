package org.infoshare.rekinyfinansjeryweb.formData;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class SaveOfFiltrationSettings extends FiltrationSettings {
    public SaveOfFiltrationSettings(){
        super();
    }
    @NotBlank(message = "{filtration.saving.not.blank.name}")
    private String preferenceName;
}