package com.example.LMSApp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.LMSApp.Dao.UserDaoService;
import com.example.LMSApp.model.User;


@RestController
public class UserController {
	
	@Autowired
	UserDaoService userDaoService;
	
	
	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listUser() {

		HashMap<String, Object> entities = new HashMap();
		entities.put("Status", "Authenticated");
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		entities.put("Time", formatter.format(new Date()));

		return ResponseEntity.ok(entities);
	}
	
	@PostMapping("/user/register")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {

		HashMap<String, Object> entities = new HashMap();
		User ent = null;
		if (validateEmail(user.getEmail()) && validatePassword(user.getPassword())) {
			if (null == userDaoService.findUser(user.getEmail())) {
				ent = userDaoService.save(user);
				//byte[] enc = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				entities.put("Message:", "User is registered");
				return new ResponseEntity<>(entities,HttpStatus.CREATED);
			} else {
				entities.put("Message", "User already exists !!");
				return new ResponseEntity<>(entities, HttpStatus.FORBIDDEN);
			}
		} else {
			entities.put("Invalid Format","Please input correct format for email id and/or a Password with atleast 8 chars including 1 number and a special char");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}
	}
	
	public Boolean validatePassword(String password) {
		if (password != null || (!password.equalsIgnoreCase(""))) {
			String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
			return (password.matches(pattern));
		} else {
			return Boolean.FALSE;
		}

	}

	public Boolean validateEmail(String email) {
		if (email != null || (!email.equalsIgnoreCase(""))) {
			String emailvalidator = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
					+ "A-Z]{2,7}$";

			return email.matches(emailvalidator);
		} else {
			return Boolean.FALSE;
		}

	}
	
	
}
