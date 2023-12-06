package com.Shop.Configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.Shop.Utils.Properties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
	private static FirebaseApp firebaseApp;
	@Autowired
	Properties properties;

	@PostConstruct
	public void init() throws IOException {
		if (firebaseApp == null) {
			ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
			// System.out.println(properties.getBucketName());
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
					.setStorageBucket(properties.getBucketName()).build();
			firebaseApp = FirebaseApp.initializeApp(options);

		}

	}

	@Bean
	public FirebaseApp firebaseApp() {
		return firebaseApp;
	}

}
