package org.iboutsikas.practicals.bookstore.entities;

import javax.persistence.*;


@Entity
@Table(name = "books")
@NamedQueries({
	@NamedQuery(name=Book.GET_ALL, query="SELECT e FROM Book e"),
	@NamedQuery(name=Book.GET_BY_AUTHOR, query="SELECT e FROM Book e WHERE e.author LIKE :authorName"),
	@NamedQuery(name=Book.GET_BY_ID, query="SELECT b FROM Book b WHERE b.id = :id"),
	@NamedQuery(name=Book.FUZZY_SEARCH, query="SELECT b FROM Book b WHERE lower(b.author) LIKE :term OR lower(b.language) LIKE :term OR lower(b.publisher) LIKE :term OR lower(b.subject) LIKE :term OR lower(b.title) LIKE :term OR lower(b.isbn) LIKE :term")
})
public class Book {
	
	public static final String GET_ALL = "getAll";
	public static final String GET_BY_AUTHOR = "getByAuthor";
	public static final String GET_BY_ID = "getByID";
	public static final String FUZZY_SEARCH = "fuzzySearch";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String author;
	@Column(nullable = false)
	private String language;
	@Column(nullable = false)
	private String publisher;
	@Column(nullable = false)
	private String subject;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String isbn;
	private boolean inStock;
	
	public Book() {
		
	}
	
	
	
	public Book(String author, String language, String publisher, String subject, String title, String isbn,
			boolean inStock) {
		super();
		this.author = author;
		this.language = language;
		this.publisher = publisher;
		this.subject = subject;
		this.title = title;
		this.isbn = isbn;
		this.inStock = inStock;
	}



	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public boolean isInStock() {
		return inStock;
	}
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public int getId() {
		return id;
	}
}
