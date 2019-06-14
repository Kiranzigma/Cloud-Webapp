package com.example.LMSApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.LMSApp.model.ImageBook;

@Repository
public interface ImageRepository extends JpaRepository<ImageBook, String> {
	
	@Query(value = "SELECT * FROM image_book b WHERE b.id = ?1", nativeQuery = true)
	ImageBook getImageById(String book_id);
	
	@Query(value = "DELETE  FROM image_book b WHERE b.id = ?1", nativeQuery = true)
	void deleteImageById(String id);
}
