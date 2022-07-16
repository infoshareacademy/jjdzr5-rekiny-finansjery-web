package org.infoshare.rekinyfinansjeryweb.dto.user;

import lombok.Data;

import java.util.*;

@Data
public class UserDTO {

    private UUID id;
    private String email;
    private String name;
    private String lastname;
    private String billingCurrency;
}
