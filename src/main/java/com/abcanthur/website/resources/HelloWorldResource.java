package com.abcanthur.website.resources;

import static com.abcanthur.website.codegen.Tables.USERS;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;
import org.jooq.Result;

import com.abcanthur.website.codegen.Tables;
import com.abcanthur.website.codegen.tables.records.UsersRecord;
import com.abcanthur.website.api.Saying;
import com.codahale.metrics.annotation.Timed;

@Path("/a")
public class HelloWorldResource {
	private final String template;
	private final String defaultName;
	private final AtomicLong counter;
	
	public HelloWorldResource(String template, String defaultName) {
		this.template = template;
		this.defaultName = defaultName;
		this.counter = new AtomicLong();
	}
	
	@GET
	@Path("/hello-world")
	@Produces(MediaType.TEXT_PLAIN)
	@Timed
	public String nested(@QueryParam("name") Optional<String> name, @Context DSLContext database) {
		final String value = String.format(template, name.orElse(defaultName));
		return value;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/what")
	@Timed
	public Saying sayHello(@QueryParam("name") Optional<String> name, @Context DSLContext database) {
		UsersRecord user = database.selectFrom(Tables.USERS).fetch().get(0);
		String record = user.getEmail() + ": " + user.getPassword();
		//final String value = String.format(template, name.orElse(defaultName));
		return new Saying(counter.incrementAndGet(), record);
	}
}
