package org.iboutsikas.practicals.bookstore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.iboutsikas.practicals.bookstore.entities.Book;
import org.iboutsikas.practicals.bookstore.repositories.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.dbunit.rules.DBUnitRule;
import com.github.dbunit.rules.api.configuration.DBUnit;
import com.github.dbunit.rules.api.dataset.DataSet;
import com.github.dbunit.rules.util.EntityManagerProvider;

@RunWith(JUnit4.class)
@DBUnit(leakHunter = true)
public class BookRepositoryTest {

	private BookRepository repo;

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Rule
	public EntityManagerProvider emProvider = EntityManagerProvider
			.instance("org.iboutsikas.practicals.bookstore.tests.entityManager");

	@Rule
	public DBUnitRule dbUnitRule = DBUnitRule.instance(emProvider.connection());

	@Before
	public void setUp() {
		repo = new BookRepository(emProvider.em());
	}

	@After
	public void tearDown() {

	}

	@Test
	@DataSet("books.yml")
	public void itShouldGetAllTheBooks() {
		List<Book> books = repo.getAll();

		assertNotNull(books);
		assertThat(books.size(), is(not(0)));
		assertThat(books.size(), is(3));
	}

	@Test
	@DataSet("books.yml")
	public void itShouldGetTheBookWithID() {
		Book book = repo.find(1);

		assertNotNull(book);
		assertThat(book.getId(), is(1));
	}

	@Test(expected = NoResultException.class)
	@DataSet("books.yml")
	public void itShouldNotFindTheBook() {
		Book book = repo.find(5);
	}

	@Test
	@DataSet("books.yml")
	public void itShouldSearchAndFind3Books() {

		String term = "Java".toLowerCase();

		List<Book> books = repo.search(term);
		
		assertNotNull(books);
		assertThat(books.size(), is(not(0)));
		assertThat(books.size(), is(3));
		
		Book first = books.get(0);
		
		assertEquals(first.getTitle(), "Learning Java fast");
		
		Book second = books.get(1);
		
		assertEquals(second.getTitle(), "Failing at learning Java fast");
	}
	
	@Test
	@DataSet("books-searchable.yml")
	public void itShouldSearchAndFind2Books() {

		String term = "Giannakis".toLowerCase();

		List<Book> books = repo.search(term);
		
		assertNotNull(books);
		assertThat(books.size(), is(not(0)));
		assertThat(books.size(), is(3));
		
		Book first = books.get(0);
		
		assertEquals(first.getAuthor(), "Giannakis");
		
		Book second = books.get(1);
		
		assertEquals(second.getPublisher(), "Giannakis");
	}

	@Test
	@DataSet("books.yml")
	public void itShouldCreateANewBook() {
		Book newBook = new Book("Authoras", "Gibberish", "Publicatoras", "Thamatara", "Titlos gia pola pola", "65888", true);
		
		Book persistedBook = repo.create(newBook);
		
		assertEquals(persistedBook.getAuthor(), newBook.getAuthor());
		assertEquals(persistedBook.getLanguage(), newBook.getLanguage());
		assertEquals(persistedBook.getPublisher(), newBook.getPublisher());
		assertEquals(persistedBook.getSubject(), newBook.getSubject());
		assertEquals(persistedBook.getTitle(), newBook.getTitle());
		assertEquals(persistedBook.getIsbn(), newBook.getIsbn());
		assertEquals(persistedBook.isInStock(), newBook.isInStock());
		
	}
	
	@Test(expected = PersistenceException.class)
	@DataSet("books.yml")
	public void itShouldThrowWhenCreatingABookWithMissingProperties() {
		Book newBook = new Book();
		
		newBook.setAuthor("Authoras");
		
		repo.create(newBook);
	}
	
	@Test
	@DataSet("books.yml")
	public void itShouldDeleteTheBook() {
		repo.delete(2);
		
		thrown.expect(NoResultException.class);
		repo.find(2);		
	}
	
	@Test
	@DataSet("books.yml")
	public void itShouldUpdateTheBook() {
		Book book = repo.find(2);
		
		book.setAuthor("Changed Authoras");
		
		Book result = repo.update(book);
		
		assertEquals(result.getAuthor(), "Changed Authoras");
	}
	
	@Test
	@DataSet("books.yml")
	public void itShouldThrowIfEntityDoesNotExistOnUpdate() {
		Book book = repo.find(3);
		repo.delete(3);
		
		book.setAuthor("Changed");
		
		thrown.expect(NoResultException.class);
		repo.update(book);
	}
	
	@Test
	@DataSet("books.yml")
	public void itShouldGetAllTheBooksInStock() {
		List<Book> results = repo.getAllInStock();
		
		assertNotNull(results);
		assertEquals(results.size(), 2);
		
		for (Book book : results) {
			if(!book.isInStock()) {
				fail("There should be no out of stock book in the list");
			}
		}
	}
	
	@Test
	@DataSet("books-searchable.yml")
	public void itShouldSearchOnlyInStockBooks() {
		List<Book> results = repo.searchInStock("Prog");

		assertNotNull(results);
		assertEquals(2, results.size());
		
		for (Book book : results) {
			if(!book.isInStock()) {
				fail("There should be no out of stock book in the list");
			}
		}		
	}
}
