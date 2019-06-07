package com.example.LMSApp.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LMSApp.config.PasswordEncoderConfig;
import com.example.LMSApp.model.User;
import com.example.LMSApp.repository.UserRepository;

@Service
public class UserDaoServiceImpl implements UserDaoService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoderConfig passwordEncoderConfig;
	
	@Override
	public User findUser(String email) {
		return userRepository.findUser(email);
	}

	@Override
	public User save(User users) {

		users.setPassword(passwordEncoderConfig.customPasswordEncoder().encode(users.getPassword()));

		return userRepository.save(users);
	}
}
