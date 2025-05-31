package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.JsonUtils;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetAllUserWithDesrializationTest extends BaseTest {
	
	
	private String tokenId;

	@BeforeClass
	public void setUpToken() {
		tokenId = "1df4c7fffe33c9869711b441146b680ec2dca1f356b715fe9984321662af43a5";
		ConfigManager.set("bearertoken", tokenId);
	}
	
	@Test
	public void createAUserTest() {
		User user = new User(null, "Priyanka", StringUtils.getRandomEmailId(), "female", "active");
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Priyanka");
		Assert.assertNotNull(response.jsonPath().getString("id"));	
		
		String userId = response.jsonPath().getString("id");
		
		//2. GET: fetch the user using the same user id:
		Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseGet.statusLine().contains("OK"));		

		User userResponse = JsonUtils.desrialize(responseGet, User.class);
		Assert.assertEquals(userResponse.getName(), user.getName());
				
		
	}
	

}
