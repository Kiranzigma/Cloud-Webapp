package com.example.LMSApp.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LMSApp.model.ImageBook;
import com.example.LMSApp.repository.ImageRepository;


@Service
public class ImageDaoServiceImp implements ImageDaoService{
	
	@Autowired
	ImageRepository imageRepository;

	@Override
	public List<ImageBook> findAll() {
		// TODO Auto-generated method stub
		return imageRepository.findAll();
	}

	@Override
	public ImageBook createImageBook(ImageBook book) {
		// TODO Auto-generated method stub
		return imageRepository.save(book);
	}

	@Override
	public ImageBook getImageBookById(String bookId) {
		// TODO Auto-generated method stub
		return imageRepository.getImageById(bookId);
	}

	@Override
	public ImageBook updateImageBook(ImageBook imagebookDetails) {
		// TODO Auto-generated method stub
		return imageRepository.save(imagebookDetails);
	}

	@Override
	public void deleteImageBook(String id) {
		// TODO Auto-generated method stub
		imageRepository.deleteById(id);
		
	}
	
	@Override
	public void deleteImageById(String bookId) {
		// TODO Auto-generated method stub
		 imageRepository.deleteImageById(bookId);
	}
}
