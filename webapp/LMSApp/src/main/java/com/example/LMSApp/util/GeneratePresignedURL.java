package com.example.LMSApp.util;



import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@Service
public class GeneratePresignedURL {

	@Value("${bucketname}")
	private String bucketName;

	public String generatePreSignedUrl(String objectKey) {
                
    	 AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient(); 
 
         // Set the presigned URL to expire after one hour.
         java.util.Date expiration = new java.util.Date();
         long expTimeMillis = expiration.getTime();
         expTimeMillis += 1000 * 2 * 60;
         expiration.setTime(expTimeMillis);

         // Generate the presigned URL.
         System.out.println("Generating pre-signed URL.");
         
         GeneratePresignedUrlRequest generatePresignedUrlRequest = 
                 new GeneratePresignedUrlRequest(bucketName, objectKey)
                 .withMethod(HttpMethod.GET)
                 .withExpiration(expiration);
         URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);        
         System.out.println("Pre-Signed URL: " + url.toString());
         return url.toString();
     
   
	
 }
}
