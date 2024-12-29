package com.app.movie.trade.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsAppConfig {
	@Value("${aws.access.key}")
	private String accessKey;
	@Value("${aws.secret.key}")
	private String secretKey;
	@Value("${aws.region}")
	private String region;

	@Bean
	public S3Client s3Client() {
		S3Client s3Client = S3Client.builder().region(Region.of(this.region))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(this.accessKey, this.secretKey))).build();
		return s3Client;
	}
}
