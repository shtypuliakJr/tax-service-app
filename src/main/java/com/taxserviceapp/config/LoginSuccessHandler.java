package com.taxserviceapp.config;

import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.data.entity.UserRole;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String redirectURL = request.getContextPath();

        for (GrantedAuthority auth :  userDetails.getAuthorities()) {
            if (auth.getAuthority().equals(UserRole.USER.getAuthority())) {
                redirectURL = "/user/user";
                System.out.println("here user");
            } else if (auth.getAuthority().equals(UserRole.INSPECTOR.getAuthority())) {
                redirectURL = "/inspector/inspector";
                System.out.println("here inspector");
            }
        }

        System.out.println("redirectURL " + redirectURL);
        if (redirectURL == null) {
            throw new IllegalStateException();
        }
//        new DefaultRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, redirectURL);
//
        response.sendRedirect(redirectURL);
//        if (userDetails.getAuthorities().contains(UserRole.USER)) {
//            redirectURL = "/user/user";
//            System.out.println("here");
//        } else if (userDetails.getAuthorities().equals(UserRole.INSPECTOR)) {
//            redirectURL = "/inspector/inspector";
//        }
//        System.out.println(redirectURL);
//        response.sendRedirect(redirectURL);


    }
}
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//        String redirectURL = null;
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        authorities.forEach(System.out::println);
//
//        for (GrantedAuthority ga : authorities) {
//            if (ga.getAuthority().equals(UserRole.USER)) {
//                redirectURL = "user/user";
//                break;
//            } else if (ga.getAuthority().equals(UserRole.INSPECTOR)) {
//                redirectURL = "/inspector/inspector";
//                break;
//            }
//        }
//        System.out.println("redirectURL " + redirectURL);
//        if (redirectURL == null) {
//            throw new IllegalStateException();
//        }
//        new DefaultRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, redirectURL);
//    }

