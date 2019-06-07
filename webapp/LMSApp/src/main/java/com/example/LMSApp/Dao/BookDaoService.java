package com.example.LMSApp.Dao;

import java.util.List;
import com.example.LMSApp.model.Book;

public interface BookDaoService  {

	

	List<Book> findAll();

	Book createBook(Book book);

	public Book getBookById(String bookId);

	Book updateBook(Book bookDetails);

	public void deleteBook(Book bookDetails);
	
	
}
