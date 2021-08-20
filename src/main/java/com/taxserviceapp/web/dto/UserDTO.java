package com.taxserviceapp.web.dto;

import com.taxserviceapp.data.entity.Personality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NotNull
    @Size(min = 2, max = 20, message = "Not valid first name")
    @NotBlank(message = "Require name")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 30, message = "Not valid last name")
    @NotBlank(message = "Require surname")
    private String lastName;

    @NotNull
    @Length(min = 6, message = "Your password should have more than 6 symbols")
    private String password;

    @NotNull
    @Email(message = "Provide valid email")
    private String email;

    @NotNull
    @Min(value = 12, message = "Age should be valid")
    private int age;

    @NotNull
    @Digits(integer = 12, fraction = 0, message = "Invalid ipn")
    private String ipn;

    @NotNull(message = "Require personality type")
    private Personality personality;

    @NotNull(message = "Require address")
    private String address;

}
