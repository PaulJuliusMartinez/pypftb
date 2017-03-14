package com.abcanthur.website.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sessions")
public class Sessions {

    @Id
    int id;

    @Column(name = "token")
    String token;

    @Column(name = "user_id")
    int userId;
    
    @Column(name = "expires_at")
    Date expiresAt;
    
}