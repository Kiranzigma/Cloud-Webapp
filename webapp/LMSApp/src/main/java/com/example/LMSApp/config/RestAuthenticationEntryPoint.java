package com.example.LMSApp.config;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.LMSApp.controller.UserController;
import com.timgroup.statsd.StatsDClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

  private final static Logger logger = LoggerFactory.getLogger(UserController.class);	
  @Autowired
  private StatsDClient statsDClient;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	String path = request.getServletPath();
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();
    JSONObject jsonObject = new JSONObject();
    if(path.contains("/book")) {
    	if(request.getMethod().equals("GET")&&path.contains("/image")) {
    		jsonObject.put("message", "Not authorized to read image");
    	}else if(request.getMethod().equals("POST")&&path.contains("/image")) {
    		jsonObject.put("message", "Not authorised to create this image for book");
    	}else if(request.getMethod().equals("PUT")&&path.contains("/image")) {
    		jsonObject.put("message", "Not authorised to update this image for book");
    	}else if(request.getMethod().equals("DELETE")&&path.contains("/image")){
    		jsonObject.put("message", "Not authorized to delete image for  book");
		}
    	else if(request.getMethod().equals("GET")) {
    		jsonObject.put("message", "Not authorized to read images for the book");
    	}else if(request.getMethod().equals("POST")) {
    		jsonObject.put("message", "Not authorised to create this book");
    	}else if(request.getMethod().equals("PUT")) {
    		jsonObject.put("message", "Not authorised to update this book");
    	}else {
    		jsonObject.put("message", "Not authorized to delete this book");
		}
	}else {
		logger.info("--Inside invalid logging--");
		statsDClient.incrementCounter("endpoint.login.http.get /");
		jsonObject.put("message", "you are not logged in");
	}
    writer.println(jsonObject.toString());
}

  @Override
  public void afterPropertiesSet() throws Exception {
    setRealmName("cc6225");
    super.afterPropertiesSet();
  }
}
