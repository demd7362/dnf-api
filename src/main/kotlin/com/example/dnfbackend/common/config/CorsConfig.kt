package com.example.dnfbackend.common.config

import com.example.dnfbackend.common.utils.SystemEnvironment
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = mutableListOf("http://taecho.xyz")
        config.allowedMethods = mutableListOf("*")
        config.allowedHeaders = mutableListOf("*")
        config.exposedHeaders = mutableListOf("*")
        config.maxAge = 3600L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config);
        return CorsFilter(source)
    }
}

