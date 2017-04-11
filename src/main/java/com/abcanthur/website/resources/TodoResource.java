package com.abcanthur.website.resources;

import java.util.List;
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
import org.jooq.impl.DSL;
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

@Path("/todos")
public class TodoResource {
	
	@POST
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
		return getTodoPojoById(database, record.getValue(TODOS.ID));
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public TodoPojo returnTodoById(
		@Context DSLContext database,
		@PathParam("id") Integer id
	) {
		return getTodoPojoById(database, id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TodoPojo> getTodoByUser(
			@Context UserRecord user,
			@Context DSLContext database
			) {
		return database
				.selectFrom(TODOS)
				.where(TODOS.USER_ID.equal(user.getId()))
				.fetch()
				.into(TodoPojo.class);
	}
	
	@POST
	@Path("/{id}/complete")
	@Produces(MediaType.APPLICATION_JSON)
	public TodoPojo completeTodo(
		@Context DSLContext database,
		@PathParam("id") Integer id
	) {
		database
			.update(TODOS)
			.set(TODOS.COMPLETED, true)
			.set(TODOS.DATE_COMPLETED, DSL.currentTimestamp())
			.where(TODOS.ID.equal(id), TODOS.COMPLETED.equal(false))
			.execute();
		return getTodoPojoById(database, id);
	}
	
	private TodoPojo getTodoPojoById(DSLContext database, Integer id) {
		return getTodoRecordById(database, id).into(TodoPojo.class);
	}
	
	private TodoRecord getTodoRecordById(DSLContext database, Integer id) {
		TodoRecord todoRecord = database
				.selectFrom(TODOS)
				.where(TODOS.ID.equal(id))
				.fetchOne();
		return todoRecord;
	}
}
