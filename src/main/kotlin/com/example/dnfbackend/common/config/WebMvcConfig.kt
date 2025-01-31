package com.example.dnfbackend.common.config

import com.example.dnfbackend.common.interceptor.ReferrerCheckInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
//class WebMvcConfig: WebMvcConfigurer {
//    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(ReferrerCheckInterceptor())
//            .addPathPatterns("/**")
//            .excludePathPatterns("/api/**")
//    }
//}
