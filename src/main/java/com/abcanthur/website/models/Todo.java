package com.abcanthur.website.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    int id;
    
    @Column(name = "date_created")
    Date dateCreated;
    
    @Column(name = "date_completed")
    Date dateCompleted;
    
    @Column(name = "body")
    String body;
    
    @Column(name = "completed")
    boolean completed;
    
    @Column(name = "parent_id")
    int parentId;
    
    @Column(name = "user_id")
    int userId;
    
}