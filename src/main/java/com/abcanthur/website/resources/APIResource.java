package com.abcanthur.website.resources;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import static com.abcanthur.website.codegen.Tables.*;

@Path("/api")
public class APIResource {

	@POST
	@Path("/pledge")
	public String pledge(
		@FormParam("name") String name,
		@FormParam("age") Integer age,
		@FormParam("location") String location,
		@FormParam("favoriteBrewer") String favoriteBrewer,
		@FormParam("lastTimePeedPants") String lastTimePeedPants,
		@FormParam("countyStadiumMemory") String countyStadiumMemory,
		@FormParam("bestPartOfBrewersFan") String bestPartOfBrewersFan,
		@FormParam("email") String email,
		@Context DSLContext database
	) {
		try {
			database.insertInto(
				PLEDGES,
				PLEDGES.NAME,
				PLEDGES.AGE,
				PLEDGES.LOCATION,
				PLEDGES.FAVORITE_BREWER,
				PLEDGES.LAST_TIME_PEED_PANTS,
				PLEDGES.COUNTY_STADIUM_MEMORY,
				PLEDGES.BEST_PART_OF_BREWERS_FAN,
				PLEDGES.EMAIL
			).values(
				name,
				age,
				location,
				favoriteBrewer,
				lastTimePeedPants,
				countyStadiumMemory,
				bestPartOfBrewersFan,
				email
			).execute();
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw new WebApplicationException("Unknown error", 400);
		}
		
		return "";
	}
}