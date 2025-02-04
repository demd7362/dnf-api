package com.example.dnfbackend.common.config

import com.fasterxml.jackson.annotation.JsonProperty


data class Credentials(
    @JsonProperty("api_key")
    val apiKey: String
)
