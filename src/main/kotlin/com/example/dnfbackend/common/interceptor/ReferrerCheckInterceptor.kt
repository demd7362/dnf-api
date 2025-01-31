package com.example.dnfbackend.common.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class ReferrerCheckInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.headerNames.toList().forEach {
            println("$it : ${request.getHeader(it)}")
        }
        val referer = request.getHeader("Referer")
        if (referer == null || !referer.startsWith("http://localhost")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return false
        }
        return true
    }
}
