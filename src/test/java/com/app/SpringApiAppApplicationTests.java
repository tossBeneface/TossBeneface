package com.app;

import com.app.api.feignTest.client.HelloClient;
import com.app.global.config.FeignConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(FeignConfiguration.class)
class SpringApiAppApplicationTests {

	@Autowired
	private HelloClient helloClient;

	@Test
	void contextLoads() {
	}

}
