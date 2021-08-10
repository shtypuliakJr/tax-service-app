package com.taxserviceapp.web.model;

import com.taxserviceapp.data.entity.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// ToDo: 1) patter + 2) error exception + 3) Size

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NonNull
    @Size(min = 2, max = 20)
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @NonNull
    private int age;

    @NonNull
    private String ipn;

    @NotNull
    private UserRole userRole;

}
