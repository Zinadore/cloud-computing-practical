package org.iboutsikas.practicals.bookstore.resources;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.iboutsikas.practicals.bookstore.entities.Book;
import org.iboutsikas.practicals.bookstore.repositories.BookRepository;



@Path("books")
@Singleton
public class BookResource {
	private BookRepository mBookRepo;
	private EntityManager em;
	
	public BookResource() {
		em = Persistence.createEntityManagerFactory("org.iboutsikas.practicals.bookstore.entityManager").createEntityManager();
		this.mBookRepo = new BookRepository(em);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@QueryParam("search") String searchTerm, @QueryParam("includeNotInStock") boolean includeNotInStock) {
		List<Book> results;
		if(searchTerm != null && !searchTerm.isEmpty() && includeNotInStock) {
			System.out.println("--------------1");
			results = this.mBookRepo.search(searchTerm);
		} else if(searchTerm != null && !searchTerm.isEmpty() && !includeNotInStock){
			System.out.println("--------------2");
			results = this.mBookRepo.searchInStock(searchTerm);
		} else if ((searchTerm == null || searchTerm.isEmpty()) && includeNotInStock) {
			System.out.println("--------------3");
			results = this.mBookRepo.getAllInStock();
		} else {
			System.out.println("--------------4");
			results = this.mBookRepo.getAll();
		}
		
		GenericEntity<List<Book>> ent = new GenericEntity<List<Book>>(results) {};
		return Response.ok().entity(ent).build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@PathParam("id") int id) {
		try {
			Book result = this.mBookRepo.find(id);
			
			return Response.status(200).entity(result).build();
		} catch(NoResultException nre) {
			return Response.status(404).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Book book) {
		try {
			book.setId(0);
			Book result = this.mBookRepo.create(book);
			
			return Response.status(201).entity(result).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Book book) {
		if(id != book.getId()) {
			return Response.status(422).build();
		}
		Book result = this.mBookRepo.update(book);
		
		return Response.ok(result).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id) {
		try {
			this.mBookRepo.delete(id);
			return Response.status(204).build();
		} catch (NoResultException nre) {
			return Response.status(404).build();
		}
	}
	
	@PreDestroy
	public void cleanUp() {
//		System.out.println("Cleaning Up");
		em.close();
	}
		
}
