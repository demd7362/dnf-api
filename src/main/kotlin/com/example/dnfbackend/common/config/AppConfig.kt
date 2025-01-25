package com.example.dnfbackend.common.config

import com.example.dnfbackend.common.constant.Constant
import com.example.dnfbackend.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriBuilderFactory

@Configuration
class AppConfig {
    private val log = logger()
    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl(Constant.BASE_URL)
            .requestInterceptor { request, body, execution ->
                log.info("Request: ${request.method} ${request.uri}")
                val start = System.currentTimeMillis()
                val response = execution.execute(request, body)
                val end = System.currentTimeMillis()
                log.info("Response: ${response.statusCode} (${end - start}ms)")
                response
            }
            .build()
    }
}
