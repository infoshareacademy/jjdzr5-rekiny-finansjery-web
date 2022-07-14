package org.infoshare.rekinyfinansjeryweb.dto.user;

import lombok.Data;
import org.infoshare.rekinyfinansjeryweb.entity.user.UserEnum;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserAdminPanelDTO {

    private String id;
    @Email(message = "{validation.email}")
    @NotBlank(message = "{validation.email.blank}")
    private String email;
    @Size(min = 8, max = 255, message = "{validation.password}")
    private String password;
    @Size(min = 3, max = 30, message = "{validation.name}")
    private String name;
    @Size(min = 3, max = 30, message = "{validation.lastname}")
    private String lastname;
    @Min(value = 0, message = "validation.billing.currency")
    private double billingCurrency;
    @NotNull
    private Set<UserEnum> role;
    private LocalDateTime createdAt;
    @NotNull
    private boolean enabled;
}
