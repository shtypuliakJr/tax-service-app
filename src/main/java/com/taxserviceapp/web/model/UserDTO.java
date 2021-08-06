package com.taxserviceapp.web.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
public class UserDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;

    private int age;

    private String ipn;

}
