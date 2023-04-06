package com.example.IliushynParser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IliushynParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(IliushynParserApplication.class, args);
		WebDriverManager.chromedriver().setup();
	}

}
