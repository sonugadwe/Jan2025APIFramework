package com.qa.api.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class SchemaValidator {

	public static boolean schemaValidator(Response response, String schemaFileName) {
		try {
			response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFileName));
			System.out.println("Schema validation passed for :"+schemaFileName);
			return true;
		} catch (Exception e) {
			System.out.println("Schema validation is failed."+e.getMessage());
			return false;
		}
	}
	
}
