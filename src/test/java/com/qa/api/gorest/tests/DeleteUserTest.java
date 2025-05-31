package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DeleteUserTest extends BaseTest{
	
	private String tokenId;

	@BeforeClass
	public void setUpToken() {
		tokenId = "1df4c7fffe33c9869711b441146b680ec2dca1f356b715fe9984321662af43a5";
		ConfigManager.set("bearertoken", tokenId);
	}
	
	//1. create a user - POST
		@Test
		public void deleteUSerTest() {
			User user = User.builder()
					.name("Rahul")
					.email(StringUtils.getRandomEmailId())
					.status("active")
					.gender("male")
					.build();
			
			Response responsePost = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertEquals(responsePost.jsonPath().getString("name"), "Rahul");
			Assert.assertNotNull(responsePost.jsonPath().getString("id"));
					
			//Fetch the user id
			String userId = responsePost.jsonPath().getString("id");
			System.out.println("User Id is: "+userId);
			
			//2. Fetch the user using same user id
			Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertTrue(responseGet.statusLine().contains("OK"));
			Assert.assertEquals(responseGet.jsonPath().getString("id"), userId);
			
			//Delete the user using same user id
			Response responseDelete= restClient.delete(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertTrue(responseDelete.statusLine().contains("No Content"));
			
			//4. Fetch the deleted user using same user id
			responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertTrue(responseGet.statusLine().contains("Not Found"));
			Assert.assertEquals(responseGet.statusCode(), 404);
			Assert.assertEquals(responseGet.jsonPath().getString("message"), "Resource not found");
		}			

}
