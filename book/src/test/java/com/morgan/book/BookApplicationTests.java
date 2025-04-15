package com.morgan.book;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-unittest.properties")
class BookApplicationTests {

	@Test
	void contextLoads() {
	}

}
