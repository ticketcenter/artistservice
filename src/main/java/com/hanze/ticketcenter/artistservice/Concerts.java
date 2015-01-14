package com.hanze.ticketcenter.artistservice;

import com.hanze.ticketcenter.artistservice.dao.ConcertsDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("concerts")
public class Concerts {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getConcerts(
            @DefaultValue("Netherlands") @QueryParam("location") String location,
            @DefaultValue("10") @QueryParam("page_size") String pageSize,
            @DefaultValue("1") @QueryParam("page_number") String pageNumber) {

        return new ConcertsDAO().getConcerts(location, pageSize, pageNumber);
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getConcert(
            @QueryParam("id") String id) {

        return new ConcertsDAO().getConcert(id);
    }
}
