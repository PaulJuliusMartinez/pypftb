package com.abcanthur.website.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pledges")
public class Pledge {

    @Id
    int id;
    
    @Column(name = "name")
    String name;
    
    @Column(name = "age")
    Integer age;

    @Column(name = "location")
    String location;

    @Column(name = "favorite_brewer")
    String favoriteBrewer;

    @Column(name = "last_time_peed_pants")
    String lastTimePeedPants;

    @Column(name = "county_stadium_memory")
    String countyStadiumMemory;

    @Column(name = "best_part_of_brewers_fan")
    String bestPartOfBrewersFan;

    @Column(name = "email")
    String email;

}
