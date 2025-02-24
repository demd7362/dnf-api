package com.taecho.dnfbackend.timeline.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime


@Entity
@Table(name = "timeline")
class Timeline(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "character_id", unique = true, columnDefinition = "VARCHAR(100)")
    @Comment("캐릭터 unique key")
    var characterId: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    @Comment("서버명")
    var server: String,

    @Column(name = "character_name", nullable = false, columnDefinition = "VARCHAR(20)")
    @Comment("캐릭터명")
    var characterName: String,

    @Column(name = "adventure_name", nullable = false, columnDefinition = "VARCHAR(20)")
    @Comment("모험단명")
    var adventureName: String,

    @Column(name = "filtered_timeline", nullable = false, columnDefinition = "TEXT")
    @Comment("필터링된 타임라인")
    var filteredTimeline: String,

    @Column(name = "updated_at", nullable = false)
    @Comment("마지막 갱신 일자")
    var updatedAt: LocalDateTime = LocalDateTime.now(),


    @OneToOne(
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE]
    )
    @JoinColumn(name = "timeline_statistics_id")
    @Comment("타임라인 통계 FK")
    var timelineStatistics: TimelineStatistics? = null
)
