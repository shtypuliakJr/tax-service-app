package com.taxserviceapp.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;


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
