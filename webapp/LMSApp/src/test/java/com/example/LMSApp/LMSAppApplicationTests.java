package com.example.LMSApp;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.LMSApp.Dao.BookDaoService;
import com.example.LMSApp.Dao.UserDaoService;
import com.example.LMSApp.controller.BookController;
import com.example.LMSApp.model.Book;
import com.example.LMSApp.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = BookController.class, secure = false)
public class LMSAppApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserDaoService userService;
   
   @MockBean
   private BookDaoService bookService;

	  @Test 
	  public void emailChecker(){
		  User user = new User();
		  user.setEmail("qwert@gmail.com");
		  user.setPassword("4ar@@@@@@");
		  
		  Mockito.when(userService.findUser(Mockito.anyString())).thenReturn(user); 
		  String testName = userService.findUser("qwert@gmail.com").getEmail();
	      Assert.assertEquals("qwert@gmail.com", testName); 
	  }
	 
	@Test
	public void retrieveBook() throws Exception { 
		 Book mockBook = new Book();
		 mockBook.setId("159b43eb-d8d0-49e0-a614-0b0ca10657bf");
		 mockBook.setAuthor("Tejas"); 
		 mockBook.setIsbn("987-63663663");
		 mockBook.setQuantity(5); 
		 mockBook.setTitle("Tejas");
		Mockito.when(
				bookService.getBookById(Mockito.anyString())).thenReturn(mockBook);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/book/159b43eb-d8d0-49e0-a614-0b0ca10657bf").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		String expected = "{id:159b43eb-d8d0-49e0-a614-0b0ca10657bf,title:Tejas,author:Tejas,isbn:987-63663663,quantity:5}";
		//assertEquals(HttpStatus.OK.value(), response.getStatus());
		JSONAssert.assertEquals(expected, response.getContentAsString(), false);
	}
   
   @Test
	public void create() throws Exception {
	   
	     Book mockBook = new Book();
		 mockBook.setAuthor("Tejas"); 
		 mockBook.setIsbn("987-63663663");
		 mockBook.setQuantity(5); 
		 mockBook.setTitle("Tejas");
		String exampleCourseJson = "{\"title\":\"Tejas\",\"author\":\"Tejas\",\"isbn\":\"987-63663663\",\"quantity\":\"5\"}";
		
		// bookService to respond back with mockBook
		Mockito.when(
				bookService.createBook(Mockito.any(Book.class))).thenReturn(mockBook);

		// Send bookDetails as body to /book
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/book/")
				.accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}
}