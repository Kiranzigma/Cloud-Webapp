package com.example.LMSApp.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

@Service
public class PasswordResetDAO {

	@Value("${cloud.snsTopic}")
	private String snsTopic;

	private static String topicArn = "";

	private static final Logger logger = LoggerFactory.getLogger(PasswordResetDAO.class);

	public void sendEmailToUser(String email) {
		logger.info("Sending mail to user using topic arn:::" + snsTopic);

		topicArn = snsTopic;

		AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

		final String msg = email;
		final PublishRequest publishRequest = new PublishRequest(snsClient.createTopic("password_reset").getTopicArn(), msg);
		final PublishResult publishResponse = snsClient.publish(publishRequest);

		logger.info("MessageId: " + publishResponse.getMessageId());

	}

}
