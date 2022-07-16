package org.infoshare.rekinyfinansjeryweb.config;

import org.infoshare.rekinyfinansjeryweb.dto.user.CreateUserFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.EditUserDataFormDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.UserDTO;
import org.infoshare.rekinyfinansjeryweb.entity.user.AuthenticationProvider;
import org.infoshare.rekinyfinansjeryweb.entity.user.CustomOAuth2User;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String name[] = oAuth2User.getName().split("[ ]");
        String email = oAuth2User.getEmail();
        if (userService.emailExists(email)) {
            EditUserDataFormDTO editUserData = userService.getEditUserData();
            editUserData.setName(name[0]);
            editUserData.setLastname(name[1]);
            editUserData.setEmail(email);
            userService.saveEditUser(editUserData);
        } else {
            CreateUserFormDTO createUser = new CreateUserFormDTO();
            createUser.setName(name[0]);
            createUser.setLastname(name[1]);
            createUser.setEmail(email);
            userService.addUserOAuth2(createUser, AuthenticationProvider.GOOGLE);
        }

        response.sendRedirect("/user");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
