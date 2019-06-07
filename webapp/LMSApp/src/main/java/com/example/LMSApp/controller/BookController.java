package com.example.LMSApp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.LMSApp.Dao.BookDaoService;
import com.example.LMSApp.Dao.UserDaoService;
import com.example.LMSApp.model.Book;


@RestController
public class BookController {

	@Autowired
	BookDaoService bookDaoServiceImpl;
	
	@Autowired
	UserDaoService  userDaoServiceImpl;
	
	
	
	@GetMapping("/book")
	public List<Book> getAllBook() {
		return bookDaoServiceImpl.findAll();
	}

	@PostMapping("/book")
	public ResponseEntity<Object> createNote(@Valid @RequestBody Book bookDetails) {
		HashMap<String, Object> entities = new HashMap<String, Object>();
		Book book  = bookDaoServiceImpl.createBook(bookDetails);
		if(book!=null) {
			entities.put("book", book);
			return new ResponseEntity<>(entities.get("book"), HttpStatus.CREATED);
		}else {
			entities.put("message", "Book details are not entered correct");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
	}
	


	@GetMapping("/book/{id}")
	public ResponseEntity<Object> getBookById(@PathVariable(value = "id") String bookId) {
		HashMap<String, Object> entities = new HashMap<String, Object>();
		Book book = bookDaoServiceImpl.getBookById(bookId);
		if (null == book) {
			entities.put("message", "Book does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		} else { 
			entities.put("book", bookDaoServiceImpl.getBookById(bookId));
			return new ResponseEntity<>(entities.get("book"),HttpStatus.OK);
		} /*
			 * else { entities.put("message", "Not authorized to read this book"); return
			 * new ResponseEntity<>(entities, HttpStatus.UNAUTHORIZED); }
			 */
	}
	
	
	
	
	
	

	
}