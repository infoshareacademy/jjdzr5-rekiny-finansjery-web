package org.infoshare.rekinyfinansjeryweb.entity.user;

import org.hibernate.annotations.Type;
import org.infoshare.rekinyfinansjeryweb.repository.Currency;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = UserCurrency.TABLE_NAME)
public class UserCurrency {

    public static final String TABLE_NAME = "user_currency";
    public static final String COLUMN_PREFIX = "uc_";

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;

    @ManyToOne
    private Currency currency;

    @Column(name = COLUMN_PREFIX + "amount")
    private double amount;

    @ManyToOne
    @JoinColumn(name = User.COLUMN_PREFIX + "id")
    private User user;

    @OneToMany(mappedBy = "userCurrency", fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    private List<OperationHistory> historyList = new ArrayList<>();

    public UserCurrency() {
    }

    public UserCurrency(Currency currency, double amount, User user, List<OperationHistory> historyList) {
        this.currency = currency;
        this.amount = amount;
        this.user = user;
        this.historyList = historyList;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OperationHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<OperationHistory> historyList) {
        this.historyList = historyList;
    }

}
