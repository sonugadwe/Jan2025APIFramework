package com.qa.api.products.tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.utils.JsonPathValidatorUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ProductAPITestWithJsonPath extends BaseTest{
	
	@Test
	public void getProductTest() {
		Response response = restClient.get(BASE_URL_PRODUCTS,PRODUCTS_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.ANY);
		Assert.assertEquals(response.getStatusCode(), 200);

		List<Number> ids = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].id");
		System.out.println("Ids:"+ids);
		
		List<Number> prices = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].price");
		System.out.println("Prices:"+prices);
		
		List<Number> rates = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].rating.rate");
		System.out.println("Rates:"+rates);
		
		List<Number> counts = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].rating.count");
		System.out.println("Count:"+counts);
		
		List<Map<String, Object>> idTitleList = JsonPathValidatorUtil.readListOfMaps(response, "$.[*].['id', 'title']");
		System.out.println(idTitleList);
		
		List<Map<String, Object>> idTitleCatList = JsonPathValidatorUtil.readListOfMaps(response, "$.[*].['id', 'title', 'category']");
		System.out.println(idTitleCatList);
		
		List<Map<String, Object>> jewlIDTitleList = JsonPathValidatorUtil.readListOfMaps(response, "$[?(@.category == 'jewelery' )].['id', 'title']");
		System.out.println(jewlIDTitleList);
		
		Double price = JsonPathValidatorUtil.read(response, "min($[*].price)");
		System.out.println("min price -->"+ price);
		
		
	}

}
