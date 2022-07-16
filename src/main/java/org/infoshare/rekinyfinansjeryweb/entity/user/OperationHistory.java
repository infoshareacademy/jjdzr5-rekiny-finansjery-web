package org.infoshare.rekinyfinansjeryweb.entity.user;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = OperationHistory.TABLE_NAME)
public class OperationHistory {

    public static final String TABLE_NAME = "operation_history";
    public static final String COLUMN_PREFIX = "oh_";

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;

    @Column(name = COLUMN_PREFIX + "date_operation")
    private LocalDateTime dateOperation;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_PREFIX + "operation")
    private OperationEnum operation;

    @Column(name = COLUMN_PREFIX + "exchange_rate")
    private double exchangeRate;

    @Column(name = COLUMN_PREFIX + "amount")
    private String amount;

    @ManyToOne
    @JoinColumn(name = User.COLUMN_PREFIX + "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = UserCurrency.COLUMN_PREFIX + "id")
    private UserCurrency userCurrency;

    public OperationHistory() {
    }

    public OperationHistory(LocalDateTime dateOpreation, OperationEnum operation, double exchangeRate, String amount) {
        this.dateOperation = dateOpreation;
        this.operation = operation;
        this.exchangeRate = exchangeRate;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOpreation) {
        this.dateOperation = dateOpreation;
    }

    public OperationEnum getOperation() {
        return operation;
    }

    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCurrency getUserCurrency() {
        return userCurrency;
    }

    public void setUserCurrency(UserCurrency userCurrency) {
        this.userCurrency = userCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationHistory that = (OperationHistory) o;

        if (Double.compare(that.exchangeRate, exchangeRate) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (dateOperation != null ? !dateOperation.equals(that.dateOperation) : that.dateOperation != null)
            return false;
        if (operation != that.operation) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return userCurrency != null ? userCurrency.equals(that.userCurrency) : that.userCurrency == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateOperation != null ? dateOperation.hashCode() : 0);
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        temp = Double.doubleToLongBits(exchangeRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (userCurrency != null ? userCurrency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OperationHistory{" +
                "dateOpreation=" + dateOperation +
                ", operation=" + operation +
                ", exchangeRate=" + exchangeRate +
                ", amount=" + amount +
                '}';
    }

}
