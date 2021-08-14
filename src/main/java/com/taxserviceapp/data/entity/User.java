package com.taxserviceapp.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails, Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "ipn", nullable = false)
    private String ipn;

    @Column(name = "date", nullable = false)
    private LocalDateTime dateOfRegistration;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "active", nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<Report> reports;

    @Builder
    public User(String firstName, String lastName, String email, String password, int age, String ipn, UserRole userRole, LocalDateTime dateOfRegistration, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.ipn = ipn;
        this.userRole = userRole;
        this.dateOfRegistration = dateOfRegistration;
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(userRole.getAuthority());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }
}
