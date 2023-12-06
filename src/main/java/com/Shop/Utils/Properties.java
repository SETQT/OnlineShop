package com.Shop.Utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class Properties {
	private String bucketName;

	public String getBucketName() {
		return this.bucketName;
	}

	@Deprecated
	@DeprecatedConfigurationProperty(replacement = "firebase.bucket-name")
	public String getTarget() {
		return this.bucketName;
	}

	@Deprecated
	public void setTarget(String target) {
		this.bucketName = target;
	}
}
