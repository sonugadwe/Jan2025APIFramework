package com.qa.api.schema.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.SchemaValidator;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoRestUserAPISchemaTest extends BaseTest{
	
	@Test
	public void getUSerAPISchemaTest() {
		
		ConfigManager.set("bearertoken", "1df4c7fffe33c9869711b441146b680ec2dca1f356b715fe9984321662af43a5");
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.ANY);
		
		Assert.assertTrue(SchemaValidator.schemaValidator(response, "schema/getUserScema.json"));
	}
	
	@Test
	public void createUserAPISchemaTest() {
		ConfigManager.set("bearertoken", "1df4c7fffe33c9869711b441146b680ec2dca1f356b715fe9984321662af43a5");

		User user = User.builder()
				.name("api")
				.status("active")
				.email(StringUtils.getRandomEmailId())
				.gender("female")
				.build();
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(SchemaValidator.schemaValidator(response, "schema/createUserScema.json"));
		
		
		
	}

}
