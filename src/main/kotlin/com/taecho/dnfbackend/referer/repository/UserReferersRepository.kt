package com.taecho.dnfbackend.referer.repository

import com.taecho.dnfbackend.referer.entity.UserReferers
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface UserReferersRepository:JpaRepository<UserReferers, Long> {

//    fun findByRefererAndVisitedAtAndIp(referer: String, visitedAt: LocalDate, ip: String)
}
