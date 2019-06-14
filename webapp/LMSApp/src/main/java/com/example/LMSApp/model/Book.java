package com.example.LMSApp.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name="book")
public class Book {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	
	private String id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String author;
	
	@NotBlank
	private String isbn;
	
	@NotNull
	@Range(min=1)
	private int quantity;
	
	@OneToOne(targetEntity = ImageBook.class, cascade=CascadeType.ALL, orphanRemoval=true)
	@PrimaryKeyJoinColumn
	private ImageBook image;
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ImageBook getImage() {
		return image;
	}

	public void setImage(ImageBook image) {
        this.image = image;
    }
	
	
	
	
}
