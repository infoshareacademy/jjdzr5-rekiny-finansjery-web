package org.infoshare.rekinyfinansjeryweb.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EditUserDataFormDTO {

    @Email(message = "{validation.email}")
    @NotBlank(message = "{validation.email.blank}")
    private String email;
    @Size(min = 3, max = 30, message = "{validation.name}")
    private String name;
    @Size(min = 3, max = 30, message = "{validation.lastname}")
    private String lastname;

}
