package org.infoshare.rekinyfinansjeryweb.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateUserFormDTO {

    @Email(message = "{validation.email}")
    @NotBlank(message = "{validation.email.blank}")
    private String email;
    @Size(min = 8, max = 32, message = "{validation.password}")
    private String password;
    @Size(min = 8, max = 32, message = "{validation.password}")
    private String repeatPassword;
    @Size(min = 3, max = 30, message = "{validation.name}")
    private String name;
    @Size(min = 3, max = 30, message = "{validation.lastname}")
    private String lastname;

}
