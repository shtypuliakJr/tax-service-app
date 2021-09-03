package com.taxserviceapp.web.dto;

import com.taxserviceapp.data.entity.Personality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @NotBlank(message = "{user.data.dto.firstName.error.null}")
    @Size(min = 2, max = 20, message = "{user.data.dto.firstName.error.length}")
    private String firstName;

    @NotBlank(message = "{user.data.dto.lastName.error.null}")
    @Size(min = 3, max = 30, message = "{user.data.dto.lastName.error.length}")
    private String lastName;

    @NotBlank(message = "{user.data.dto.password.error.null}")
    @Length(min = 6, message = "{user.data.dto.password.error.length}")
    private String password;

    @NotBlank(message = "{user.data.dto.email.error.null}")
    @Email(message = "{user.data.dto.email.error.valid}")
    private String email;

    @NotBlank(message = "{user.data.dto.age.error.null}")
    @Pattern(regexp = "[1-9][0-9]{2}", message = "{user.data.dto.age.error.valid}")
    private String age;

    @NotEmpty(message = "{user.data.dto.ipn.error.null}")
    @Pattern(regexp = "^[0-9]{12}", message = "{user.data.dto.ipn.error.valid}")
    private String ipn;

    @NotNull(message = "{user.data.personality.error}")
    private Personality personality;

    @NotBlank(message = "{user.data.dto.address.error}")
    private String address;

    private Date dateOfRegistration;

    private Long userId;


}
