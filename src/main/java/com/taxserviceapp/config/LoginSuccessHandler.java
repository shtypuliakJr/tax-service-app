package com.taxserviceapp.config;

import com.taxserviceapp.data.entity.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final GrantedAuthority inspectorAuthority = new SimpleGrantedAuthority(UserRole.INSPECTOR.getAuthority());
    private final GrantedAuthority userAuthority = new SimpleGrantedAuthority(UserRole.USER.getAuthority());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        String successfulURL = null;

        if (authentication.getAuthorities().contains(inspectorAuthority)) {
            successfulURL = "/inspector";
        } else if (authentication.getAuthorities().contains(userAuthority)) {
            successfulURL = "/user/user";
        }

        getRedirectStrategy().sendRedirect(request, response, successfulURL);
    }
}