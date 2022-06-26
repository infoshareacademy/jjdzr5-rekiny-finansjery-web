package org.infoshare.rekinyfinansjeryweb.dto.user;

import lombok.Data;
import org.infoshare.rekinyfinansjeryweb.entity.Currency;
import org.infoshare.rekinyfinansjeryweb.entity.user.User;

import java.util.UUID;

@Data
public class UserCurrencyDTO {

    private UUID id;
    private Currency currency;
    private double amount;
}
