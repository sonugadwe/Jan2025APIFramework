package com.qa.api.client;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.constants.AuthType;
import com.qa.api.exceptions.APIExceptions;
import com.qa.api.manager.ConfigManager;
import static io.restassured.RestAssured.expect;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.Base64;
import java.util.Map;

import org.testng.Assert;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestClient {

	// define Response Sepcs:
	private ResponseSpecification responseSpec200 = expect().statusCode(200);
	private ResponseSpecification responseSpec201 = expect().statusCode(201);
	private ResponseSpecification responseSpec204 = expect().statusCode(204);
	private ResponseSpecification responseSpec404 = expect().statusCode(404);
	private ResponseSpecification responseSpec400 = expect().statusCode(400);

	private ResponseSpecification responseSpec200or201 = expect().statusCode(anyOf(equalTo(200), equalTo(201)));
	private ResponseSpecification responseSpec200or404 = expect().statusCode(anyOf(equalTo(200), equalTo(404)));

	private RequestSpecification setUpRequest(String baseUrl, AuthType authType, ContentType contentType) {

		RequestSpecification request = RestAssured.given().log().all().baseUri(baseUrl).contentType(contentType)
				.accept(contentType);

		switch (authType) {
		case BEARER_TOKEN:
			request.header("Authorization", "Bearer " + ConfigManager.get("bearertoken"));
			break;
		case OAUTH2:
			request.header("Authorization", "Bearer " + "oauth2 token");
			break;
		case BASIC_AUTH:
			request.header("Authorization", "Basic "+ generateBasicAuthToken());
			break;
		case API_KEY:
			request.header("x-api-key", "api key");
			break;
		case NO_AUTH:
			System.out.println("Auth is not required...");
			break;
		default:
			System.out.println("this auth is not supported....please pass the right AuthType...");
			throw new APIExceptions("===Invalid Auth====");
		}

		return request;
	}
	
	public String generateBasicAuthToken() {
		String credentials = ConfigManager.get("basicauthusername") + ":" + ConfigManager.get("basicauthpassword");
		//admin:admin --> "YWRtaW46YWRtaW4=" (base64 encoded value)
		return Base64.getEncoder().encodeToString(credentials.getBytes());
	}
	
	public void applyParams(RequestSpecification request, Map<String, String> queryParams,
			Map<String, String> pathParams) {
		if (queryParams != null) {
			request.queryParams(queryParams);
		}
		if (pathParams != null) {
			request.pathParams(pathParams);
		}
	}

	// CRUD

	// Get
	// while going to get call at least we can check response code is 200 or not and
	// give me the response after that i will check body
	/**
	 * This method is used to call GET APIS
	 * 
	 * @param basicUrl
	 * @param endPoint
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return it returns the get API call response
	 */
	@Step("Calling get api with base url:{0}")
	public Response get(String baseUrl, String endPoint, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {
		
		ChainTestListener.log("API base url is :"+ baseUrl);
		ChainTestListener.log("Auth type :"+ authType.toString());

		
		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);
		Response response = request.get(endPoint).then().spec(responseSpec200or404).extract().response();
		response.prettyPrint();
		return response;
	}

	// POST
	// T means any type and it reserves on java it will automatic convert to it
	public <T> Response post(String baseUrl, String endPoint, T body, 
							Map<String, String> queryParams,
							Map<String, String> pathParams, 
							AuthType authType, ContentType contentType) {

		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

		applyParams(request, queryParams, pathParams);

		Response response = request.body(body).post(endPoint).then().spec(responseSpec200or201).extract().response();
		response.prettyPrint();
		return response;

	}
	
	public <T> Response post(String baseUrl, String endPoint, File file, 
			Map<String, String> queryParams,
			Map<String, String> pathParams, 
			AuthType authType, ContentType contentType) {

		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

		applyParams(request, queryParams, pathParams);

		Response response = request.body(file).post(endPoint).then().spec(responseSpec200or201).extract().response();
		response.prettyPrint();
		return response;

	}
	
	public Response post(String baseUrl, String endPoint, String clientId, String clientSecret, String grantType, ContentType contentType) {

		Response response = RestAssured.given()
				.contentType(contentType)
				.formParam("grant_type", grantType)
				.formParam("client_id", clientId)
				.formParam("client_secret", clientSecret)
				.when()
					.post(baseUrl+endPoint);
				
		response.prettyPrint();
		return response;
	}
	
	public <T> Response put(String baseUrl, String endPoint, T body, 
			Map<String, String> queryParams,
			Map<String, String> pathParams, 
			AuthType authType, ContentType contentType) {

			RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

			applyParams(request, queryParams, pathParams);

			Response response = request.body(body).put(endPoint).then().spec(responseSpec200).extract().response();
			response.prettyPrint();
			return response;

	}
	
	public <T> Response patch(String baseUrl, String endPoint, T body, 
			Map<String, String> queryParams,
			Map<String, String> pathParams, 
			AuthType authType, ContentType contentType) {

			RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

			applyParams(request, queryParams, pathParams);

			Response response = request.body(body).patch(endPoint).then().spec(responseSpec200).extract().response();
			response.prettyPrint();
			return response;

	}
	
	public <T> Response delete(String baseUrl, String endPoint, 
			Map<String, String> queryParams,
			Map<String, String> pathParams, 
			AuthType authType, ContentType contentType) {

			RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

			applyParams(request, queryParams, pathParams);

			Response response = request.delete(endPoint).then().spec(responseSpec204).extract().response();
			response.prettyPrint();
			return response;

	}

}
