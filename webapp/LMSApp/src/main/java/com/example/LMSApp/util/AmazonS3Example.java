package com.example.LMSApp.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.LMSApp.model.ImageBook;


@Service
public class AmazonS3Example {

	@Value("${bucketname}")
	private String bucketName;
	
	

	public String save(MultipartFile file) {
		//AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient(); 
		s3client.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), convertMultiPartToFile(file)));
		String url = s3client.getUrl(bucketName, file.getOriginalFilename()).toString();
		return url;
	}

	private File convertMultiPartToFile(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(convFile);

			fos.write(file.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convFile;
	}

	public void delete(String name) throws MalformedURLException {
		// TODO Auto-generated method stub
		//AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
		 URL url = new URL(name);

		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
		s3client.deleteObject(new DeleteObjectRequest(bucketName, FilenameUtils.getName(url.getPath())));
	}
}