package com.taecho.dnfbackend.common.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(basePackages = ["com.taecho.dnfbackend.*.repository"])
class DatabaseConfig {

    @Value("\${spring.profiles.active}")
    private lateinit var activeProfile: String

    @Bean
    @DependsOn("credentials")
    fun dataSource(credentials: Credentials): DataSource {
        val database = when (activeProfile) {
            "prod" -> credentials.database.prod
            "local" -> credentials.database.local
            else -> throw IllegalStateException("Unknown active profile: $activeProfile")
        }
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = database.jdbcUrl
        hikariConfig.username = database.username
        hikariConfig.password = database.password
        hikariConfig.driverClassName = database.driverClassName
        return HikariDataSource(hikariConfig)
    }
}
