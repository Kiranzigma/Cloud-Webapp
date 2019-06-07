package com.example.LMSApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.LMSApp.model.Book;



public interface BookRepository extends JpaRepository<Book, String> {

	//@Query(value = "SELECT * FROM book b", nativeQuery = true)
	List<Book> findAll();

	@Query(value = "SELECT * FROM book b WHERE b.id = ?1", nativeQuery = true)
	Book getBookById(String book_id);
	
	
}







