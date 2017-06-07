package org.iboutsikas.practicals.bookstore.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.iboutsikas.practicals.bookstore.entities.Book;

public class BookRepository {
	/*private static final String MANAGER_NAME = "org.iboutsikas.practicals.bookstore.em";*/
	private final EntityManager em;
	
	public BookRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Book> getAll(){
//		em em = Persistence.createemFactory(MANAGER_NAME).createem();
				
		em.getTransaction().begin();
		
		try {
			List<Book> result = em.createNamedQuery(Book.GET_ALL, Book.class).getResultList();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}
	
	public Book find(int id) {
		
		try {
			Book result = em.find(Book.class, id);
			
			if(result == null) {
				throw new NoResultException("Could not find book with id: " + id);
			}
			
			return result;
		} catch (Exception e) {
			throw e;
		}
	
	}
	
	public List<Book> search(String term) {
		em.getTransaction().begin();
		
		try {
			List<Book> results = em.createNamedQuery(Book.FUZZY_SEARCH, Book.class)
					.setParameter("term", "%" + term + "%")
					.getResultList();
			
			em.getTransaction().commit();
			
			return results;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}
	
	public Book create(Book newEntity) {
		em.getTransaction().begin();
		
		try {
			em.persist(newEntity);
			em.flush();
			em.refresh(newEntity);
			
			em.getTransaction().commit();
			
			return newEntity;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}
	
	public void delete(int id) {
		em.getTransaction().begin();
		
		try {
			Book entity = em.find(Book.class, id);
			
			if(entity == null) {
				em.getTransaction().rollback();
				throw new NoResultException("Could not find book with id " + id + " to delete.");
			}
			
			em.remove(entity);
			
			em.getTransaction().commit();
			
		} catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}
	
	public Book update(Book entity) {
		em.getTransaction().begin();
		
		try {
			
			//If the entity does not exist, throw so it is not created. We dont want to use update to create entities
			Book book = em.find(Book.class, entity.getId());
			if(book == null) {
				em.getTransaction().rollback();
				throw new NoResultException("Could not find book with id " + entity.getId() + " to update.");
			}
			
			Book result = em.merge(entity);
			
			em.getTransaction().commit();
			return result;
			
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}
}
