package com.abcanthur.website.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "test_table")
public class TestTable {

	@Id
	int id;
	
	@Column(name = "one_col")
	String oneCol;
	
}
