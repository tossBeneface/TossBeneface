package com.app.global;

import com.app.global.resolver.memberInfo.MemberInfo;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.app.api")) // todo API 패키지 경로 수정(패키지 추가 변동 시)
            .paths(PathSelectors.ant("/api/**")) // todo API uri 경로 수정
            .build()
            .apiInfo(apiInfo()) // API 문서에 대한 정보 추가
            .useDefaultResponseMessages(false) // swagger에서 제공하는 기본 응답 코드 설명 제거
            .securityContexts(Arrays.asList(securityContext()))
            .securitySchemes(Arrays.asList(apiKey()))
            .ignoredParameterTypes(MemberInfo.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("API 문서")
            .description("API에 대해서 설명해주는 문서입니다.")
            .version("1.0")
            .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "AccessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}
