package com.qa.api.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	private static Properties properties = new Properties();
	
	// mvn clean install -Denv=qa/stage/dev/uat/prod
			// mvn clean install -Denv=qa
			// mvn clean install -- if env is not given, then run test cases on QA env by default.
			// env -- environment variable(system)
	
	static {//it loads initially before loading config manager and it going to CMA memory
		
		String envName = System.getProperty("env", "prod");
		System.out.println("running tests on env: " + envName);
		String fileName = "config_" + envName + ".properties"; // config_qa.properties

		InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(fileName);
		//InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config/config.properties");
		if(input != null) {
			try {
				properties.load(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String get(String key) {
		return properties.getProperty(key).trim();
	}
	
	public static void set (String key, String value) {
		properties.setProperty(key, value);
	}
	
}
