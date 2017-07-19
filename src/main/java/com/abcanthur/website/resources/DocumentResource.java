package com.abcanthur.website.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;
import org.jooq.Record;

import static com.abcanthur.website.codegen.Tables.*;

import com.abcanthur.website.codegen.tables.pojos.PledgePojo;
import com.abcanthur.website.views.HomeView;
import com.abcanthur.website.views.ListView;
import com.abcanthur.website.views.MethodsView;

@Path("/")
public class DocumentResource {
	
	public static int DEFAULT_COUNT = 50;
	public static int MAX_COUNT = 100;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public HomeView home(@Context DSLContext database) {
		return new HomeView("This is the homepage of pyp");
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.TEXT_HTML)
	public ListView list(
		@Context DSLContext database,
		@QueryParam("count") Optional<Integer> countParam,
		@QueryParam("offset") Optional<Integer> offsetParam
	) {
		int count = countParam.orElse(DEFAULT_COUNT);
		int offset = offsetParam.orElse(0);
		
		// Safeguards against manually-edited queries.
		if (count < 0) count = DEFAULT_COUNT;
		if (count > MAX_COUNT) count = MAX_COUNT;
		if (offset < 0) offset = 0;
		
		System.out.println(count);
		System.out.println(offset);
		
		List<PledgePojo> pledges = database
			.selectFrom(PLEDGES)
			.limit(count)
			.offset(offset)
			.fetchInto(PledgePojo.class);
		
		Record record = database.selectCount().from(PLEDGES).fetchOne();
		int total = (Integer) record.get(0);
		System.out.println("Total: " + total);
			
		return new ListView(pledges, count, offset, total);
	}
	
	@GET
	@Path("/methods")
	@Produces(MediaType.TEXT_HTML)
	public MethodsView methods(@Context DSLContext database) {
		return new MethodsView("JJ Hardy throw to first.");
	}
	
}
