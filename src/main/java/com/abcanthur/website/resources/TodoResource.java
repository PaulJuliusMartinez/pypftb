package com.abcanthur.website.resources;

import static com.abcanthur.website.codegen.Tables.USERS;

import java.util.Optional;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

import static com.abcanthur.website.codegen.Tables.*;

import com.abcanthur.website.codegen.tables.Todos;
import com.abcanthur.website.codegen.tables.records.UsersRecord;

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
	@Produces(MediaType.TEXT_PLAIN)
	public String create(
			@FormParam("body") Optional<String> body,
			@Context UsersRecord user,
			@Context DSLContext database
	) {
		if (!body.isPresent() || body.get().equals("")) throw new WebApplicationException("Please provide a todo body", 422);
		if (user == null) throw new WebApplicationException("No active user for Todo creation", 422);
		
		try {
			database.insertInto(TODOS, TODOS.BODY, TODOS.USER_ID)
				.values(body.get(), user.getId())
				.execute();
		} catch (DataAccessException e) {
			throw new WebApplicationException("Unknown error", 400);
		}
		String createTodoLog = "todo created: " + body + " by " + user.getEmail();
		return createTodoLog;
	}

}
