package com.gmail.qwertygoog.controller;

import com.gmail.qwertygoog.model.Author;
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

@Path("/users")
@ApplicationScoped
public class AuthorResource {

    @Inject
    io.vertx.mutiny.pgclient.PgPool sqlClient;

    @Inject
    Mutiny.Session mutinySession;
    
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    // Each element will be sent as JSON
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Uni<List<Author>> get() {
        return Author.listAll(Sort.by("name"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> create(Author author) {
        return Panache.<Author>withTransaction(author::persist)
                .onItem().transform(inserted -> Response.created(URI.create("/authors/" + inserted.id)).build());
    }
}
