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

	
	
	
	
	
	

	
}