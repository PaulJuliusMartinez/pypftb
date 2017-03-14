package com.abcanthur.website.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    @Id
    int id;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

}