//package com.app.global.config;
//
//import com.warrenstrange.googleauth.GoogleAuthenticator;
//import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@RequiredArgsConstructor
//@Configuration
//public class GAuthenticatorConfig {
//
//    private final CredentialBiz credentialBiz;
//
//    @Bean
//    public GoogleAuthenticator googleAuthenticator()
//        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder googleAuthenticatorConfigBuilder = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
//                .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(60))
//                .setWindowSize(3)
//                .setCodeDigits(6)
//                .setNumberOfScratchCodes(5);
//        GoogleAuthenticator gAuth = new GoogleAuthenticator(googleAuthenticatorConfigBuilder.build());
//
//        gAuth.s
//
//}
