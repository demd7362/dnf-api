package com.taecho.dnfbackend.api.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime


@Entity
@Table(name = "timeline")
class Timeline(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "character_id", unique = true)
    @Comment("캐릭터 unique key")
    var characterId: String,

    @Column(nullable = false)
    @Comment("서버명")
    var server: String,
    @Column(nullable = false)
    @Comment("캐릭터명")
    var characterName: String,

    @Column(name = "adventure_name", nullable = false)
    @Comment("모험단명")
    var adventureName: String,

    @Column(name = "filtered_timeline", nullable = false, columnDefinition = "TEXT")
    @Comment("필터링된 타임라인")
    var filteredTimeline: String,

    @Column(name = "updated_at", nullable = false)
    @Comment("마지막 갱신 일자")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
