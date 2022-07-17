package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.entity.user.CustomOAuth2User;
import org.infoshare.rekinyfinansjeryweb.entity.user.User;
import org.infoshare.rekinyfinansjeryweb.entity.user.UserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserService userService;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user, getUserRole(user.getAttribute("email")));
    }

    private List<SimpleGrantedAuthority> getUserRole(String email) {
        User user = userService.getUserByEmail(email);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (user != null) {
            Set<UserEnum> roles = user.getRole();
            roles.forEach(e -> authorities.add(new SimpleGrantedAuthority(e.name())));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserEnum.ROLE_USER.name()));
        }
        return authorities;
    }
}
