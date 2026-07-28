package com.farm2biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This is the "front door" of the whole backend.
// Running this file's main() method starts an embedded web server
// (Tomcat) on port 8080 and wires up every @Controller, @Service,
// @Repository class we write, automatically.
@SpringBootApplication
public class Farm2BizApplication {
	public static void main(String[] args) {
		SpringApplication.run(Farm2BizApplication.class, args);
	}
}
