package com.example.LMSApp.controller;

import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.example.LMSApp.util.AmazonS3Example;
import com.example.LMSApp.util.GeneratePresignedURL;


@RestController
public class BookController {

	@Autowired
	ImageDaoService  imageDaoServiceImpl;

	@Autowired
	BookDaoService bookDaoServiceImpl;

	@Autowired
	UserDaoService  userDaoServiceImpl;

	@Value("${environment}")
	private String environment;

	private final StorageService storageService;

	@Autowired
	AmazonS3Example cloudStorage;

	@Autowired
	GeneratePresignedURL genPreUrl;

	@Autowired
	public BookController(StorageService storageService) {
		this.storageService = storageService;
	}



	@GetMapping("/book")
	public List<Book> getAllBook() throws MalformedURLException {

		if(environment.equals("local")) {
			return bookDaoServiceImpl.findAll();
		}
		else {
			List<Book> output = bookDaoServiceImpl.findAll();

			List<Book> needed = new ArrayList<Book>();


			for(Book book : output) {
				Book neededBook = new Book();
				ImageBook neededImage = new ImageBook();

				neededBook.setAuthor(book.getAuthor());
				neededBook.setId(book.getId());
				neededBook.setIsbn(book.getIsbn());
				neededBook.setQuantity(book.getQuantity());
				neededBook.setTitle(book.getTitle());

				if(book.getImage()!=null) {
					URL url = new URL(book.getImage().getPath());	 
					String preUrl = genPreUrl.generatePreSignedUrl(FilenameUtils.getName(url.getPath()));

					neededImage.setId(book.getImage().getId());
					neededImage.setPath(preUrl);

					neededBook.setImage(neededImage);
				}
				needed.add(neededBook);
			}

			return  needed;
		}
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
	public ResponseEntity<Object> getBookById(@PathVariable(value = "id") String bookId) throws MalformedURLException {
		HashMap<String, Object> entities = new HashMap<String, Object>();
		Book book = bookDaoServiceImpl.getBookById(bookId);
		if (null == book) {
			entities.put("message", "Book does not exists");
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		} else { 
			if(environment.equals("local")) {
				entities.put("book", bookDaoServiceImpl.getBookById(bookId));
				return new ResponseEntity<>(entities.get("book"),HttpStatus.OK);
			}
			else {
				Book neededBook = new Book();
				ImageBook neededImage = new ImageBook();

				neededBook.setAuthor(book.getAuthor());
				neededBook.setId(book.getId());
				neededBook.setIsbn(book.getIsbn());
				neededBook.setQuantity(book.getQuantity());
				neededBook.setTitle(book.getTitle());
				
				if(book.getImage()!=null) {
				URL url = new URL(book.getImage().getPath());	 
				String preUrl = genPreUrl.generatePreSignedUrl(FilenameUtils.getName(url.getPath()));

				neededImage.setId(book.getImage().getId());
				neededImage.setPath(preUrl);

				neededBook.setImage(neededImage);
				}
				entities.put("book", neededBook);
				return new ResponseEntity<>(entities.get("book"),HttpStatus.OK);
			}

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
	public ResponseEntity<?> deleteBook(@PathVariable(value = "id") String bookId) throws MalformedURLException {
		Book book = bookDaoServiceImpl.getBookById(bookId);
		HashMap<String, Object> entities = new HashMap<String, Object>();
		if (null == book) {
			entities.put("message", "Book does not exists");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		} 
		else{
			bookDaoServiceImpl.deleteBook(book);
			cloudStorage.delete(book.getImage().getPath());
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
			String name;
			String fileName = null;

			System.out.println("Here----");
			
			if(book!=null) {
				if(book.getImage()==null) {
					if(environment.equals("local")) {
						name = storageService.store(file);
						fileName = path +"/uploads/" + name;
					}
					else {
						fileName =	cloudStorage.save(file);
					}
					ImageBook imageDetails = new ImageBook();
					imageDetails.setPath(fileName);
					imageDetails.setId(book.getId());
					book.setImage(imageDetails);        
					

					bookDaoServiceImpl.updateBook(book);


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
	public ResponseEntity<Object> getImageById(@PathVariable(value = "idBook") String bookId,@PathVariable(value = "idImage") String imageId) throws MalformedURLException {
		HashMap<String, Object> entities = new HashMap<String, Object>();
		Book book = bookDaoServiceImpl.getBookById(bookId);

		if(book!=null) {
			if(book.getImage()!=null) {   

				if(environment.equals("local")) {
					entities.put("image", imageDaoServiceImpl.getImageBookById(book.getImage().getId()));
				}
				else {
					URL url = new URL(book.getImage().getPath());	 
					String preUrl = genPreUrl.generatePreSignedUrl(FilenameUtils.getName(url.getPath()));
					ImageBook needed = new ImageBook();
					needed.setId(book.getImage().getId());
					needed.setPath(preUrl);
					entities.put("image",needed);					
				}
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

			String name;
			String fileName = null;

			if (null == book) {
				entities.put("message", "Book does not exists");
				return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
			}
			else{
				if(book.getImage()!=null) {			
					if(environment.equals("local")) {
						name = storageService.store(file);
						fileName = path +"/uploads/" + name;
					}
					else {
						fileName =	cloudStorage.save(file);
					}
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
				if(environment.equals("local")) {
					book.setImage(null);
					bookDaoServiceImpl.updateBook(book);
					entities.put("Deleted", "Image was successfuly deleted");	
				}
				else {
					try {
						cloudStorage.delete(book.getImage().getPath());
						book.setImage(null);
						bookDaoServiceImpl.updateBook(book);
						entities.put("Deleted", "Image was successfuly deleted");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			else
				entities.put("message", "Image doesn't exist for this book");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);	
		}

	}
}