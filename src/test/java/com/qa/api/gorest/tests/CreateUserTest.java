package com.qa.api.gorest.tests;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AppConstants;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.ExcelUtil;
import com.qa.api.utils.StringUtils;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("Epic 100: Go Rest API fearture")
@Story("Us 100:feature go rest api - get user api")
public class CreateUserTest extends BaseTest {

	private String tokenId;

	@BeforeClass
	public void setUpToken() {
		tokenId = "1df4c7fffe33c9869711b441146b680ec2dca1f356b715fe9984321662af43a5";
		ConfigManager.set("bearertoken", tokenId);
	}

	@DataProvider
	public Object[][] getUserData() {
		return new Object[][] { { "Priyanka", "female", "active" }, { "Revathi", "female", "inactive" },
				{ "Ranjit", "male", "active" } };
	}

	@DataProvider
	public Object[][] getUSerDataFromExcel() {
		return ExcelUtil.readData(AppConstants.CREATE_USER_SHEET_NAME);
	}

	@Description("Create User using data provider....")
	@Owner("NAL")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "getUserData")
	public void createAUserTestUsingDataProvider(String name, String gender, String status) {
		User user = new User(null, name, StringUtils.getRandomEmailId(), gender, status);

		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), name);
		Assert.assertEquals(response.jsonPath().getString("gender"), gender);
		Assert.assertEquals(response.jsonPath().getString("status"), status);

		Assert.assertNotNull(response.jsonPath().getString("id"));
		ChainTestListener.log("User id :" + response.jsonPath().getString("id"));
	}

	@Description("Create User using excel....")
	@Owner("NAL")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "getUSerDataFromExcel")
	public void createAUserTestUsingExcelData(String name, String gender, String status) {
		User user = new User(null, name, StringUtils.getRandomEmailId(), gender, status);

		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), name);
		Assert.assertEquals(response.jsonPath().getString("gender"), gender);
		Assert.assertEquals(response.jsonPath().getString("status"), status);

		Assert.assertNotNull(response.jsonPath().getString("id"));
		ChainTestListener.log("User id :" + response.jsonPath().getString("id"));
	}

	@Description("Create user")
	@Owner("Sonu")
	@Severity(SeverityLevel.CRITICAL)
	@Test
	public void createAUserTest() {
		User user = new User(null, "Priyanka", StringUtils.getRandomEmailId(), "female", "active");

		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Priyanka");
		Assert.assertNotNull(response.jsonPath().getString("id"));
		ChainTestListener.log("User id :" + response.jsonPath().getString("id"));
	}

	@Test
	public void createAUserWithJsonStringTest() {
		String emailId = StringUtils.getRandomEmailId();

		String userJson = "{\n" + "\"name\": \"Sonug\",\n" + " \"email\": \"" + emailId + "\",\n"
				+ "\"gender\": \"female\",\n" + "\"status\": \"inactive\"\n" + "}";
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Sonug");
		Assert.assertNotNull(response.jsonPath().getString("id"));
	}

	@Test
	public void createAUserWithJsonFileTest() {

		File file = new File(
				"C:\\Users\\hp\\eclipse-workspace\\JAnAPI2025RestAssuredFW\\src\\test\\resources\\jsons\\user.json");

		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, file, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "SamG");
		Assert.assertNotNull(response.jsonPath().getString("id"));
	}
}
