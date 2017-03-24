package com.abcanthur.website.resources;

import java.util.Optional;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

import static com.abcanthur.website.codegen.Tables.*;

import com.abcanthur.website.codegen.tables.pojos.TodoPojo;
import com.abcanthur.website.codegen.tables.records.TodoRecord;
import com.abcanthur.website.codegen.tables.records.UserRecord;

//id 				serial PRIMARY KEY,
//date_created  	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
//date_completed 	timestamp,
//body				text NOT NULL,
//completed			boolean DEFAULT false,
//parent_id			integer REFERENCES todos (id) ON DELETE cascade,
//user_id 			integer NOT NULL REFERENCES users (id) ON DELETE cascade

@Path("/todo")
public class TodoResource {
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public TodoPojo create(
			@FormParam("body") Optional<String> body,
			@Context UserRecord user,
			@Context DSLContext database
	) {
		if (!body.isPresent() || body.get().equals("")) throw new WebApplicationException("Please provide a todo body", 422);
		if (user == null) throw new WebApplicationException("No active user for Todo creation", 422);
		
		Record record;
		try {
			record = database
				.insertInto(TODOS, TODOS.BODY, TODOS.USER_ID)
				.values(body.get(), user.getId())
				.returning(TODOS.ID)
				.fetchOne();
		} catch (DataAccessException e) {
			throw new WebApplicationException("Unknown error", 400);
		}
		TodoPojo todo = database.selectFrom(TODOS)
				.where(TODOS.ID.equal(record.getValue(TODOS.ID)))
				.fetchOne()
				.into(TodoPojo.class);
		return todo;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public TodoPojo getTodoById(
		@Context DSLContext database,
		@PathParam("id") Integer id
	) {
		TodoPojo todoPojo = database
				.selectFrom(TODOS)
				.where(TODOS.ID.equal(id))
				.fetchOne()
				.into(TodoPojo.class);
		return todoPojo;
	}	
}
