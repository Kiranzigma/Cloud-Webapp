package com.example.LMSApp.Dao;

import java.util.List;


import com.example.LMSApp.model.ImageBook;

public interface ImageDaoService {
	
	List<ImageBook> findAll();

	ImageBook createImageBook(ImageBook book);

	public ImageBook getImageBookById(String l);

	ImageBook updateImageBook(ImageBook imagebookDetails);

	public void deleteImageBook(String id);
	
	public void deleteImageById(String id);

}
