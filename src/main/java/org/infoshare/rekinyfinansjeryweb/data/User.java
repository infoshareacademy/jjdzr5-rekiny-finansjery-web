package org.infoshare.rekinyfinansjeryweb.data;

import org.hibernate.annotations.Type;
import org.infoshare.rekinyfinansjeryweb.formData.FiltrationSettings;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_PREFIX = "u_";

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = COLUMN_PREFIX + "id")
    private UUID id;

    @Email(message = "{validation.email}")
    @NotBlank(message = "{validation.email.blank}")
    @Column(name = COLUMN_PREFIX + "email")
    private String email;

    @Size(min = 8, max = 255, message = "{validation.password}")
    @Column(name = COLUMN_PREFIX + "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = UserEnum.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "id"))
    @Column(name = COLUMN_PREFIX + "role")
    private Set<UserEnum> role;

    @Size(min = 3, max = 30, message = "{validation.name}")
    @Column(name = COLUMN_PREFIX + "name")
    private String name;

    @Size(min = 3, max = 30, message = "{validation.lastname}")
    @Column(name = COLUMN_PREFIX + "lastname")
    private String lastname;

    @Column(name = COLUMN_PREFIX + "enabled")
    private boolean enabled;

    @Column(name = COLUMN_PREFIX + "billing_currency")
    private double billingCurrency;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OperationHistory> historyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserCurrency> myCurrencies = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "filtration_sett_mapping",
            joinColumns = {@JoinColumn(name = COLUMN_PREFIX + "id", referencedColumnName = COLUMN_PREFIX + "id")})//,
    @MapKeyColumn(name = "name")
    private Map<String, FiltrationSettings> savedFiltrationSettings = new HashMap<>();

    @Column(name = COLUMN_PREFIX + "create_at")
    private LocalDateTime createdAt;

    @Column(name = COLUMN_PREFIX + "last_login")
    private LocalDateTime lastLogin;

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Set<UserEnum> getRole() {
        return role;
    }

    public void setRole(Set<UserEnum> role) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getBillingCurrency() {
        return billingCurrency;
    }

    public void setBillingCurrency(double billingCurrency) {
        this.billingCurrency = billingCurrency;
    }

    public List<OperationHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<OperationHistory> historyList) {
        this.historyList = historyList;
    }

    public Set<UserCurrency> getMyCurrencies() {
        return myCurrencies;
    }

    public void setMyCurrencies(Set<UserCurrency> myCurrencies) {
        this.myCurrencies = myCurrencies;
    }

    public Map<String, FiltrationSettings> getSavedFiltrationSettings() {
        return savedFiltrationSettings;
    }

    public void setSavedFiltrationSettings(Map<String, FiltrationSettings> savedFiltrationSettings) {
        this.savedFiltrationSettings = savedFiltrationSettings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
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

        if (enabled != user.enabled) return false;
        if (Double.compare(user.billingCurrency, billingCurrency) != 0) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null) return false;
        if (historyList != null ? !historyList.equals(user.historyList) : user.historyList != null) return false;
        if (myCurrencies != null ? !myCurrencies.equals(user.myCurrencies) : user.myCurrencies != null) return false;
        if (savedFiltrationSettings != null ? !savedFiltrationSettings.equals(user.savedFiltrationSettings) : user.savedFiltrationSettings != null)
            return false;
        if (createdAt != null ? !createdAt.equals(user.createdAt) : user.createdAt != null) return false;
        return lastLogin != null ? lastLogin.equals(user.lastLogin) : user.lastLogin == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        temp = Double.doubleToLongBits(billingCurrency);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (historyList != null ? historyList.hashCode() : 0);
        result = 31 * result + (myCurrencies != null ? myCurrencies.hashCode() : 0);
        result = 31 * result + (savedFiltrationSettings != null ? savedFiltrationSettings.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
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
                ", enabled=" + enabled +
                ", billingCurrency=" + billingCurrency +
                ", historyList=" + historyList +
                ", myCurrencies=" + myCurrencies +
                ", savedFiltrationSettings=" + savedFiltrationSettings +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
