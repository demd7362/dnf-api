package com.taecho.dnfbackend.referer.service

import com.taecho.dnfbackend.common.utils.HttpUtils
import com.taecho.dnfbackend.referer.entity.UserReferers
import com.taecho.dnfbackend.referer.repository.UserReferersRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.time.LocalDate


@Service
class UserReferersService(private val userReferersRepository: UserReferersRepository) {

    fun saveReferer(){
        val referer = HttpUtils.getReferer()
        val ip = HttpUtils.getClientIp()
        if(ip.isNullOrEmpty()){
            return
        }
        val entity = UserReferers(
            referer = referer,
            ip = ip
        )
        userReferersRepository.save(entity)
    }

}
