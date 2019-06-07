package com.example.LMSApp.Dao;

import com.example.LMSApp.model.User;

public interface UserDaoService {
	
	public User findUser(String email);
	
	public User save(User users);
}
