package com.taxserviceapp.web.model;

import com.taxserviceapp.data.entity.UserRole;
import lombok.*;
import org.hibernate.validator.constraints.Length;
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
    @Size(min = 3, max = 30, message = "Not valid last name")
    @NotBlank(message = "Require surname")
    private String lastName;

    @NonNull
    @Length(min = 6, message = "Your password should have more than 6 symbols")
    private String password;

    @NotEmpty
    @Email(message = "Provide valid email")
    private String email;

    @NonNull
    @Min(value = 12, message = "Age should be valid")
    private int age;

    @NonNull
    @Pattern(regexp = "[0-9]{12}", message = "IPN must contain 12 digits")
    private String ipn;
}
