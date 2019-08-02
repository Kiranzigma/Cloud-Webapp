package com.example.LMSApp.Dao;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.LMSApp.Dao.PasswordResetDAO;

@Service
public class PasswordResetService {
	
	@Autowired
	private PasswordResetDAO passwordResetDAO;

	private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

	public ResponseEntity<Object> sendResetEmail(String email) {
		logger.info("Sending reset email:::" + email);
		if (!validateEmail(email)) {
			HashMap<String, Object> entities = new HashMap<String, Object>();
			entities.put("Validation Error",
					"Please input correct email id and/or a strong Password with atleast 8 chars including 1 number and a special char");

			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		} else {
			passwordResetDAO.sendEmailToUser(email);
			logger.info("Email successfully sent");
			return new ResponseEntity<Object>(null, HttpStatus.CREATED);
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
