package com.app.external.oauth.service;

import com.app.domain.member.constant.MemberType;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SocialLoginApiServiceFactory {

    // kakao~~랑 네이버~~~가 맵에 각각 저장될 것임
    // 키 : 빈의 이름, 밸류 : 각 소셜로그인 서비스 구현체
    private static Map<String, SocialLoginApiService> socialLoginApiServices;

    // 맵을 생성자로 이 클래스에 주입해주기
    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
        this.socialLoginApiServices = socialLoginApiServices;
    }

    // 필요한 구현체를 꺼내서 사용하기
    public static SocialLoginApiService getSocialLoginApiService(MemberType memberType) {
        String socialLoginApiServiceBeanName = "";

        if (MemberType.KAKAO.equals(memberType)) {
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
        }

        return socialLoginApiServices.get(socialLoginApiServiceBeanName);
    }
}
