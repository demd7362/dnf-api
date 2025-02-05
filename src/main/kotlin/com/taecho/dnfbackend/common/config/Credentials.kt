package com.taecho.dnfbackend.common.config

import com.fasterxml.jackson.annotation.JsonProperty


data class Credentials(
    @JsonProperty("api_key")
    val apiKey: String,
    val database: Database
) {
    data class Database(
        val prod: DatabaseProperties,
        val local: DatabaseProperties
    ) {
        data class DatabaseProperties(
            @JsonProperty("driver_class_name")
            val driverClassName: String,
            @JsonProperty("jdbc_url")
            val jdbcUrl: String,
            val password: String,
            val username: String
        )
    }
}
