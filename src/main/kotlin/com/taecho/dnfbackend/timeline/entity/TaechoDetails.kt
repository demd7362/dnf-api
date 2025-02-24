package com.taecho.dnfbackend.timeline.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
@Table(name = "taecho_details")
class TaechoDetails (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // 영속성 전이(cascade) X -> 타임라인 id만 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeline_id")
    @Comment("타임라인 FK")
    var timeline: Timeline,

    @Column(name = "item_name", columnDefinition = "VARCHAR(100)")
    @Comment("아이템명")
    var itemName: String,

    @Column(columnDefinition = "VARCHAR(30)")
    @Comment("획득 채널")
    var channel: String,

    @Column(name = "acquired_at", nullable = false)
    @Comment("아이템 획득일")
    var acquiredAt: LocalDateTime
)
