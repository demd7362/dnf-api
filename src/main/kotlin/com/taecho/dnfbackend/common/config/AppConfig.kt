package com.taecho.dnfbackend.common.config

import com.taecho.dnfbackend.common.constant.Api
import com.taecho.dnfbackend.common.utils.SystemEnvironment
import com.taecho.dnfbackend.logger
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import java.nio.file.Files
import java.nio.file.Path

@Configuration
class AppConfig(private val objectMapper: ObjectMapper) {
    private val log = logger()
    @Bean("credentials")
    fun deserializeCredentials(): Credentials {
        val path: Path = when {
            SystemEnvironment.isWindows() -> Path.of("config.json") // 루트 디렉토리
            SystemEnvironment.isLinux() -> Path.of("etc/config.json")
            else -> throw RuntimeException("Unsupported OS")
        }

        val json = Files.readString(path)
        return objectMapper.readValue(json, Credentials::class.java)
    }

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl(Api.BASE_URL)
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
