package com.example.dnfbackend.api.service

import com.example.dnfbackend.api.dto.ApiResponseDto
import com.example.dnfbackend.api.dto.CharacterDto
import com.example.dnfbackend.api.dto.TimelineResponseDto
import com.example.dnfbackend.common.constant.Constant
import com.example.dnfbackend.common.utils.DateUtils
import com.example.dnfbackend.logger
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class DnfApiService(private val restClient: RestClient, private val channels: List<String>) {
    private final val log = logger()
    companion object {
        private val CARD_DUNGEONS = setOf("모독 : 적막의 회랑", "환란 : 별내림 숲", "달이 잠긴 호수", "꿈결 속 솔리다리스", "애쥬어 메인", "죽음의 여신전", "꿈결 속 흰 구름 계곡")
        private val HELL_DUNGEONS = setOf("종말의 숭배자", "심연 : 종말의 숭배자")
    }

    fun searchCharacter(serverId: String, characterName: String): CharacterDto {
        val result = restClient.get()
            .uri("/df/servers/$serverId/characters?characterName=$characterName&apikey=${Constant.API_KEY}")
            .retrieve()
            .body(object : ParameterizedTypeReference<ApiResponseDto<CharacterDto>>() {})
        return result!!.rows[0] // 서버, 캐릭터 검색이므로 [0]

    }

    fun searchTimeline(serverId: String, characterName: String): List<TimelineResponseDto.TimeLine.TimelineRow> {
        val charactorDto = searchCharacter(serverId, characterName)
        val result = restClient.get()
            .uri("/df/servers/$serverId/characters/${charactorDto.characterId}/timeline?apikey=${Constant.API_KEY}&limit=100&code=505,513&start=2025-01-09 00:00&end=${DateUtils.getCurrentDate()}") // 던전 드랍, 카드 보상
            .retrieve()
            .body(TimelineResponseDto::class.java)
        val filtered = result!!.timeline.rows.filter {
            val isHell = it.code == 505 && it.data.dungeonName in HELL_DUNGEONS
            val isCard = it.code == 513 && it.data.dungeonName in CARD_DUNGEONS
            isCard || isHell
        }

        return filtered
    }
    fun randomChannel(): String {
        return channels.random()
    }

}
