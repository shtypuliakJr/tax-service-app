package com.taxserviceapp.config;

import com.taxserviceapp.data.entity.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final GrantedAuthority inspectorAuthority = new SimpleGrantedAuthority(UserRole.INSPECTOR.getAuthority());
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        String inspectorUrl = "/inspector/inspector";
        String userSuccessUrl = "/user/user";

        if (authentication.getAuthorities().contains(inspectorAuthority)) {
            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, inspectorUrl);
        } else {

            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, userSuccessUrl);
        }
    }
}