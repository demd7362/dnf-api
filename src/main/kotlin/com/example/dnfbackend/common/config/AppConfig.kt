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
    @Bean
    fun channels():List<String> {
        val channels = mutableListOf<String>()
        for (i in 1..12) {
            channels.add("벨 마이어 공국 $i")
            channels.add("지벤 황국 $i")
            channels.add("바하이트 $i")
            channels.add("백해 $i")
            if(i <= 10){
                channels.add("마계 $i")
            }
        }
        for( i in 40 .. 60){
            channels.add("중천 $i")
        }
        channels.shuffle()
        return channels
    }
}
