package com.taxserviceapp.web.model;

import com.taxserviceapp.data.entity.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.Valid;
import javax.validation.constraints.*;

// ToDo: 1) patter + 2) error exception + 3) Size

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NonNull
    @Size(min = 3, max = 20, message = "Not valid first name")
    @NotBlank(message = "Require name")
    private String firstName;

    @NonNull
    @Size(min = 3, max = 30)
    private String lastName;

    @NonNull
    @Size(min = 2, max = 20)
    private String password;

    @NonNull
    private String email;

    @NonNull
    @Min(value = 0, message = "Age should be valid")
    private int age;

    @NonNull
    private String ipn;
}
