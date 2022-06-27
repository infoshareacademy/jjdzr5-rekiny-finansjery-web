package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveOfFiltrationSettingsDTO extends FiltrationSettingsDTO {
    public SaveOfFiltrationSettingsDTO(){
        super();
    }
    @NotBlank(message = "{filtration.saving.not.blank.name}")
    private String preferenceName;
}