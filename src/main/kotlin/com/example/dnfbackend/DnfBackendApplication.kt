package com.example.dnfbackend

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class DnfBackendApplication
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

fun main(args: Array<String>) {
    runApplication<DnfBackendApplication>(*args)
}
