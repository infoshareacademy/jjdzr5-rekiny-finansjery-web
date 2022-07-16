package org.infoshare.rekinyfinansjeryweb.config;

import org.infoshare.rekinyfinansjeryweb.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/oauth2/**", "/currency/**", "/styles/**", "/assets/**", "/js/**", "/tables", "/table/**", "/search", "/signup", "/login", "/stats/**").permitAll()
                .antMatchers("/admin-panel", "/admin", "/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/user", false)
                    .failureUrl("/login?error=true")
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint().userService(oAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }
}
