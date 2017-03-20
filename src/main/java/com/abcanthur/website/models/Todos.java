package com.abcanthur.website.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todos")
public class Todos {

    @Id
    int id;
    
    @Column(name = "date_created")
    Date date_created;
    
    @Column(name = "date_completed")
    Date date_completed;
    
    @Column(name = "body")
    String body;
    
    @Column(name = "completed")
    boolean completed;
    
    @Column(name = "parent_id")
    int parent_id;
    
    @Column(name = "user_id")
    int userId;
    
}