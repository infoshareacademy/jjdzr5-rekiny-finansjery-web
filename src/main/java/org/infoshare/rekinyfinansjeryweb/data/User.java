package org.infoshare.rekinyfinansjeryweb.data;

import java.time.LocalDateTime;
import java.util.Map;

public class User {
    private long id;
    private String email;
    private String password;
    private UserEnum role;
    private String name;
    private String lastname;
    private double billingCurrency;
    private Map<String,Currency> myCurrencies;
    private LocalDateTime created;
    private LocalDateTime lastLogin;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEnum getRole() {
        return role;
    }

    public void setRole(UserEnum role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public double getBillingCurrency() {
        return billingCurrency;
    }

    public void setBillingCurrency(double billingCurrency) {
        this.billingCurrency = billingCurrency;
    }

    public Map<String, Currency> getMyCurrencies() {
        return myCurrencies;
    }

    public void setMyCurrencies(Map<String, Currency> myCurrencies) {
        this.myCurrencies = myCurrencies;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (Double.compare(user.billingCurrency, billingCurrency) != 0) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (role != user.role) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null) return false;
        if (myCurrencies != null ? !myCurrencies.equals(user.myCurrencies) : user.myCurrencies != null) return false;
        if (created != null ? !created.equals(user.created) : user.created != null) return false;
        return lastLogin != null ? lastLogin.equals(user.lastLogin) : user.lastLogin == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        temp = Double.doubleToLongBits(billingCurrency);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (myCurrencies != null ? myCurrencies.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", billingCurrency=" + billingCurrency +
                ", myCurrencies=" + myCurrencies +
                ", created=" + created +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
