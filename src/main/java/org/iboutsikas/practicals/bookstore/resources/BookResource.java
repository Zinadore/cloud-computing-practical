package org.iboutsikas.practicals.bookstore.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.iboutsikas.practicals.bookstore.entities.Book;
import org.iboutsikas.practicals.bookstore.repositories.BookRepository;


@Path("books")
public class BookResource {
	private BookRepository mBookRepo;
	
	public BookResource() {
//		this.mBookRepo = new BookRepository("org.iboutsikas.practicals.bookstore.entityManager");
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Book> getAll() {
		return this.mBookRepo.getAll();
	}
	
}
