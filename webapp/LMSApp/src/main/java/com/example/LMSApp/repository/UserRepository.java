package com.example.LMSApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.LMSApp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query(value= "SELECT * FROM user u WHERE LOWER(u.email) = LOWER(?1)", nativeQuery = true)
    User findUser(String email);
}
