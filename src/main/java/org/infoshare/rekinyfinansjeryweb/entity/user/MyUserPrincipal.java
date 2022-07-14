package org.infoshare.rekinyfinansjeryweb.entity.user;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyUserPrincipal implements UserDetails {
    private User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<UserEnum> roles = user.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(e -> authorities.add(new SimpleGrantedAuthority(e.name())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser(){
        return user;
    }

    public void setBillingCurrency(String billingCurrency) {
        this.user.setBillingCurrency(billingCurrency);
    }

    public void setName(String name) {
        this.user.setName(name);
    }

    public void setLastname(String lastname) {
        this.user.setLastname(lastname);
    }

}
