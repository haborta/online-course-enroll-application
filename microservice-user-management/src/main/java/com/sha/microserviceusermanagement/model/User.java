package com.sha.microserviceusermanagement.model;
import jakarta.persistence.*;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    // role enum class
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
}
