package com.example.dnfbackend.common.config

import com.example.dnfbackend.common.constant.Constant
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class AppConfig {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl(Constant.BASE_URL)
            .build()
    }
}
