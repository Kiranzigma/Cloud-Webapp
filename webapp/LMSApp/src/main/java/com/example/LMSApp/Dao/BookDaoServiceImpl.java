package com.example.LMSApp.Dao;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LMSApp.model.Book;
import com.example.LMSApp.repository.BookRepository;

@Service
public class BookDaoServiceImpl implements BookDaoService {

		@Autowired
		BookRepository bookRepository;

		@Override
		public List<Book> findAll() {
			// TODO Auto-generated method stub
			return bookRepository.findAll();
		}

		@Override
		public Book createBook(Book book) {
			// TODO Auto-gene
//			UUID uuid = UUID.randomUUID();
//			book.setId(uuid.toString());
			return bookRepository.save(book);
		}

		@Override
		public Book updateBook(Book bookDetails) {
			// TODO Auto-generated method stub
			return bookRepository.save(bookDetails);
		}

		@Override
		public void deleteBook(Book bookDetails) {
			// TODO Auto-generated method stub
			bookRepository.delete(bookDetails);
		}

		@Override
		public Book getBookById(String bookId) {
			// TODO Auto-generated method stub
			return bookRepository.getBookById(bookId);
		}


	}


