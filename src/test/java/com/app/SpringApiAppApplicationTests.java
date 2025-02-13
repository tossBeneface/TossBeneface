package com.app;

import com.app.global.config.FeignConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@Import(FeignConfiguration.class)
class SpringApiAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
