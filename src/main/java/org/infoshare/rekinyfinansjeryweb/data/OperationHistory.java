package org.infoshare.rekinyfinansjeryweb.data;

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
    private double amount;

    @ManyToOne
    @JoinColumn(name = User.COLUMN_PREFIX + "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = UserCurrency.COLUMN_PREFIX + "id")
    private UserCurrency userCurrency;

    public OperationHistory() {
    }

    public OperationHistory(LocalDateTime dateOpreation, OperationEnum operation, double exchangeRate, double amount) {
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
        if (Double.compare(that.amount, amount) != 0) return false;
        if (dateOperation != null ? !dateOperation.equals(that.dateOperation) : that.dateOperation != null)
            return false;
        return operation == that.operation;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = dateOperation != null ? dateOperation.hashCode() : 0;
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        temp = Double.doubleToLongBits(exchangeRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
