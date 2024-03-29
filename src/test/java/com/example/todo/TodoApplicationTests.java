package com.example.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
class TodoApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	@DisplayName("토큰서명 해시값 생성")
	void makeSecretKey() {
		SecureRandom random = new SecureRandom();
		byte[] key= new byte[64]; //64byte = 512bits
		random.nextBytes(key);
		String encodedKey = Base64.getEncoder().encodeToString(key);
		System.out.println("-------------------------------------------------");
		System.out.println("encodedKey = " + encodedKey);
		System.out.println("-------------------------------------------------");
	}

}
