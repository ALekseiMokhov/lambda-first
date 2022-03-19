package com.gmail.qwertygoog.controller;

import com.gmail.qwertygoog.model.Book;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import java.net.URI;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.resteasy.reactive.RestSseElementType;

@Path("/books")
@ApplicationScoped
public class BookResource {

    @Inject
    io.vertx.mutiny.pgclient.PgPool sqlClient;

    @Inject
    Mutiny.Session mutinySession;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    // Each element will be sent as JSON
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Uni<List<Book>> get() {
        return Book.listAll(Sort.by("name"));
    }

/*    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Consumes(MediaType.APPLICATION_JSON)
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Uni<Book> getByName(String name){return Book.find());}*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> create(Book book) {
        return Panache.<Book>withTransaction(book::persist)
                .onItem().transform(inserted -> Response.created(URI.create("/books/" + inserted.id)).build());
    }
}
