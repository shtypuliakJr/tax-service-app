package com.taxserviceapp.config;

import com.taxserviceapp.business.service.UserService;
import com.taxserviceapp.data.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/", "/main", "/registration").permitAll()
                .antMatchers("/inspector/**").hasAuthority(UserRole.INSPECTOR.getAuthority())
                .antMatchers( "/user/**", "/user/report-form").hasAuthority(UserRole.USER.getAuthority())
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login").permitAll()
                .successHandler(successHandler())
                .failureUrl("/login?error")
                .usernameParameter("email")
                .passwordParameter("password");

        http.logout()
                .permitAll()
                .logoutSuccessUrl("/")
                .deleteCookies()
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

}
