package com.qa.api.gorest.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetUserTest extends BaseTest {
	
	private String tokenId;

	@BeforeClass
	public void setUpToken() {
		tokenId = "1df4c7fffe33c9869711b441146b680ec2dca1f356b715fe9984321662af43a5";
		ConfigManager.set("bearertoken", tokenId);
	}
	
	@Test
	public void getAllUsersTest() {
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
		
	}
	
	@Test
	public void getAllUsersWithQueryPramTest() {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("name", "naveen");
		queryParams.put("status", "active");
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, queryParams, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
		
	}
	
	@Test
	public void getSingleUser() {
		String userId="7896410";
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(response.statusLine().contains("OK"));
		Assert.assertEquals(response.jsonPath().getString("id"), userId);

	}
	

}
