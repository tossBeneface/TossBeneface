package com.app.api.payment;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class IamportConfig {

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient("test_ck_lpP2YxJ4K877JAdv7KX8RGZwXLOb", "test_sk_d26DlbXAaV0xQbpa7y1VqY50Q9RB");
    }
}
