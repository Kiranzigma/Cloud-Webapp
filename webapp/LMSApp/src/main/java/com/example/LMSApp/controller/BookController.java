package com.example.LMSApp.controller;

import java.io.FileReader;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.LMSApp.Dao.BookDaoService;
import com.example.LMSApp.Dao.ImageDaoService;
import com.example.LMSApp.Dao.UserDaoService;
import com.example.LMSApp.model.Book;
import com.example.LMSApp.model.ImageBook;
import com.example.LMSApp.repository.ImageRepository;
import com.example.LMSApp.storage.StorageService;


@RestController
public class BookController {
	
	@Autowired
	ImageDaoService  imageDaoServiceImpl;

	@Autowired
	BookDaoService bookDaoServiceImpl;
	
	@Autowired
	UserDaoService  userDaoServiceImpl;
	
	
	
	private final StorageService storageService;
	
	@Autowired
    public BookController(StorageService storageService) {
        this.storageService = storageService;
    }
	
	
	
	@GetMapping("/book")
	public List<Book> getAllBook() {
		return bookDaoServiceImpl.findAll();
	}

	@PostMapping("/book")
	public ResponseEntity<Object> createBook(@Valid @RequestBody Book bookDetails ) {
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
		} 	
	}
	
	
	@PutMapping("/book")
	public ResponseEntity<Object> updateBook(@Valid @RequestBody Book bookDetails) {
		// Check if user authenticated and authorized
		Book book = bookDaoServiceImpl.getBookById(bookDetails.getId());
		HashMap<String, Object> entities = new HashMap<String, Object>();
		if (null == book) {
			entities.put("message", "Book does not exists");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
		else{
		
			book.setTitle(bookDetails.getTitle());
			book.setIsbn(bookDetails.getIsbn());
			book.setAuthor(bookDetails.getAuthor());
			book.setQuantity(bookDetails.getQuantity());
			Book updatedBook = bookDaoServiceImpl.updateBook(book);
			entities.put("book", updatedBook);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		} 

	}
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable(value = "id") String bookId) {
		Book book = bookDaoServiceImpl.getBookById(bookId);
		HashMap<String, Object> entities = new HashMap<String, Object>();
		if (null == book) {
			entities.put("message", "Book does not exists");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		} 
		else{
			bookDaoServiceImpl.deleteBook(book);
			entities.put("Deleted", "Book was successfuly deleted");
			return new ResponseEntity<>(entities, HttpStatus.NO_CONTENT);
		}
	}
	
	   @PostMapping("/book/{id}/image")
	   @ResponseBody
	    public ResponseEntity<Object> uploadFile(@PathVariable(value = "id") String bookId,@RequestParam("file") MultipartFile file) {
		    //System.out.println("Content  Type---"+file.getContentType());
		    HashMap<String, Object> entities = new HashMap<String, Object>();
		    if(file.getContentType().equals("image/jpeg")||file.getContentType().equals("image/png")||file.getContentType().equals("image/jpg")) {
		    
		   	Book book = bookDaoServiceImpl.getBookById(bookId);
		   
		   	String path = System.getProperty("user.dir");
		   	System.out.println("Path---"+path);
		   	
	        String name = storageService.store(file);
	        
	        String fileName = path +"/uploads/" + name;
	        
	        System.out.println("Here----");
	        //System.out.println("Here----"+book.getImage());
	        //System.out.println(book.getImageBook().getImage_id());
	        if(book!=null) {
	        if(book.getImage()==null) {
	        ImageBook imageDetails = new ImageBook();
	        imageDetails.setPath(fileName);
	        imageDetails.setId(book.getId());
	        book.setImage(imageDetails);        
	        //imageDetails.setBook(book);
	        //imageDetails.setId("1");
	        //imageDaoServiceImpl.createImageBook(imageDetails);
	        
	        bookDaoServiceImpl.updateBook(book);
	        
	        //imageDetails = imageDaoServiceImpl.createImageBook(imageDetails);
	        	     	        
	        entities.put("image", imageDaoServiceImpl.getImageBookById(book.getImage().getId()));
			return new ResponseEntity<>(entities.get("image"), HttpStatus.OK);
	        }
	        else
	        	entities.put("message", "Book already has image");
	        	return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST); 
	        }
	        else
	        	entities.put("message", "Book doesn't exists");
	        	return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST); 
	    }
		    else
		    	entities.put("message", "File-Type not supported");
        		return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST); 
	   }
	   @GetMapping("/book/{idBook}/image/{idImage}")
	   @ResponseBody
	    public ResponseEntity<Object> getImageById(@PathVariable(value = "idBook") String bookId,@PathVariable(value = "idImage") String imageId) {
		    HashMap<String, Object> entities = new HashMap<String, Object>();
		   	Book book = bookDaoServiceImpl.getBookById(bookId);
		   
	        if(book!=null) {
	        if(book.getImage()!=null) {        
	        entities.put("image", imageDaoServiceImpl.getImageBookById(book.getImage().getId()));
			return new ResponseEntity<>(entities.get("image"), HttpStatus.OK);
	        }
	        else
	        entities.put("message", "Image doesn't exist for this book");
			return new ResponseEntity<>(entities, HttpStatus.OK);
	        }
	        else
	        	entities.put("message", "Book doesn't exists");
	        	return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST); 
	    }
	   
	   @PutMapping("/book/{idBook}/image/{idImage}")
		public ResponseEntity<Object> updateImage(@PathVariable(value = "idBook") String bookId,@PathVariable(value = "idImage") String imageId,@RequestParam("file") MultipartFile file) {
			// Check if user authenticated and authorized
		   HashMap<String, Object> entities = new HashMap<String, Object>();
		   if(file.getContentType().equals("image/jpeg")||file.getContentType().equals("image/png")||file.getContentType().equals("image/jpg")) {
			Book book = bookDaoServiceImpl.getBookById(bookId);
			
			String path = System.getProperty("user.dir");
		   	System.out.println("Path---"+path);
		   	
	        String name = storageService.store(file);
	        
	        String fileName = path +"/uploads/" + name;
	        
			if (null == book) {
				entities.put("message", "Book does not exists");
				return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
			}
			else{
				if(book.getImage()!=null) {			
				ImageBook imageDetails = new ImageBook();
				imageDetails = imageDaoServiceImpl.getImageBookById(imageId);
				imageDetails.setPath(fileName);
		        imageDetails.setId(book.getId());
				book.setImage(imageDetails);
				Book updatedBook = bookDaoServiceImpl.updateBook(book);
				entities.put("book", updatedBook);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
				else
				entities.put("message", "Image doesn't exist for this book");
				return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);					

			}		
		    }		   
		        else
			    entities.put("message", "File-Type not supported");
	        	return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST); 
		    }
	   
	   @DeleteMapping("/book/{idBook}/image/{idImage}")
		public ResponseEntity<?> deleteImage(@PathVariable(value = "idBook") String bookId,@PathVariable(value = "idImage") String imageId) {
			Book book = bookDaoServiceImpl.getBookById(bookId);
			HashMap<String, Object> entities = new HashMap<String, Object>();
			if (null == book) {
				entities.put("message", "Book does not exists");
				return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
			} 
			else{
				if(book.getImage()!=null) {	
				//System.out.println("1--"+imageDaoServiceImpl.getImageBookById(book.getId()));
				//System.out.println("2--"+ book.getImage());
				//bookDaoServiceImpl.deleteBook(book);				
				//imageDaoServiceImpl.deleteImageBook(imageDaoServiceImpl.getImageBookById(book.getId()));
				//imageDaoServiceImpl.deleteImageBook(imageId);
				//ImageBook imageDetails = new ImageBook();
				//imageDetails = null;
				//imageDetails.setPath(null);
		        //imageDetails.setId(null);
				book.setImage(null);
				bookDaoServiceImpl.updateBook(book);
				entities.put("Deleted", "Image was successfuly deleted");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
				else
				entities.put("message", "Image doesn't exist for this book");
				return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);	
		}
	   
}
}