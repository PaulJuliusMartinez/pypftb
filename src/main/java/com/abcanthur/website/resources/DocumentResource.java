package com.abcanthur.website.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;

import com.abcanthur.website.views.HomeView;
import com.abcanthur.website.views.ListView;
import com.abcanthur.website.views.MethodsView;

@Path("/")
public class DocumentResource {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public HomeView home(@Context DSLContext database) {
		return new HomeView("This is the homepage of pyp");
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.TEXT_HTML)
	public ListView list(@Context DSLContext database) {
		return new ListView("This is where you can see all the pee-ers");
	}
	
	@GET
	@Path("/methods")
	@Produces(MediaType.TEXT_HTML)
	public MethodsView methods(@Context DSLContext database) {
		return new MethodsView("JJ Hardy throw to first.");
	}
	
}
