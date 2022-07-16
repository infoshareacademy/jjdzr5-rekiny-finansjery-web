package org.infoshare.rekinyfinansjeryweb.dto.user;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ChangeUserPasswordFormDTO {

    @NotEmpty
    private String oldPassword;
    @Size(min = 8, max = 32, message = "{validation.password}")
    private String newPassword;
    @Size(min = 8, max = 32, message = "{validation.password}")
    private String repeatNewPassword;
}
