package com.taecho.dnfbackend.referer.entity

import jakarta.persistence.*
import java.time.LocalDate

@Table(
    name = "user_referers"
)
@Entity
class UserReferers(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val referer: String?,

    @Column
    val ip: String,

    @Column(name = "visited_at")
    val visitedAt: LocalDate = LocalDate.now(),

    )
