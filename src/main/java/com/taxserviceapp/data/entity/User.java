package com.taxserviceapp.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "ipn")
    private String ipn;

    @Column(name = "date")
    private LocalDateTime dateOfRegistration;

    public User() {

    }
    public User(String firstName, String lastName, String username, String password, String email, int age, String ipn, LocalDateTime dateOfRegistration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.ipn = ipn;
        this.dateOfRegistration = dateOfRegistration;
    }


}
