package com.taecho.dnfbackend.common.utils

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.InetAddress


object HttpUtils {
    fun getResponse(): HttpServletResponse? {
        return getAttributes().response
    }

    fun getRequest(): HttpServletRequest {
        return getAttributes().request
    }

    private fun getAttributes(): ServletRequestAttributes {
        return RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
    }

    fun getClientIp(): String? {
        val request: HttpServletRequest = getRequest()
        var ipAddress = request.getHeader("X-Forwarded-For")
        if ("unknown".equals(ipAddress, ignoreCase = true)) {
            ipAddress = request.getHeader("Proxy-Client-IP")
        }
        if ("unknown".equals(ipAddress, ignoreCase = true)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP")
        }
        if ("unknown".equals(ipAddress, ignoreCase = true)) {
            ipAddress = request.remoteAddr
            if (ipAddress == "127.0.0.1" || ipAddress == "0:0:0:0:0:0:0:1") {
                val inetAddress = InetAddress.getLocalHost()
                ipAddress = inetAddress.hostAddress
            }
        }
        if (ipAddress != null && ipAddress.length > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
        }
        return ipAddress
    }

    fun getReferer(): String? {
        return getRequest().getHeader("referer")
    }
}
