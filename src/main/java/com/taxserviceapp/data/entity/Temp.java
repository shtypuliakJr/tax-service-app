package com.taxserviceapp.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "temp")
public class Temp implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "username")
    private String username;

    public Temp(String first_name, String last_name, String username) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
    }

    public Temp() {

    }
}
