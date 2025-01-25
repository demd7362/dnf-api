package com.example.dnfbackend.api.service

import com.example.dnfbackend.api.dto.ApiResponseDto
import com.example.dnfbackend.api.dto.CharacterDto
import com.example.dnfbackend.api.dto.TimelineResponseDto
import com.example.dnfbackend.common.constant.Constant
import com.example.dnfbackend.logger
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class DnfApiService(private val restClient: RestClient) {
    private final val log = logger()

    fun searchCharacter(serverId: String, characterName: String): CharacterDto {
        val result = restClient.get()
            .uri("/df/servers/prey/characters?characterName=$characterName&apikey=${Constant.API_KEY}")
            .retrieve()
            .body(object : ParameterizedTypeReference<ApiResponseDto<CharacterDto>>() {})
        return result!!.rows[0] // 서버, 캐릭터 검색이므로 [0]

    }

    fun searchTimeline(serverId: String, characterName: String): TimelineResponseDto {
        val charactorDto = searchCharacter(serverId, characterName)
        val result = restClient.get()
            .uri("/df/servers/$serverId/characters/${charactorDto.characterId}/timeline?apikey=${Constant.API_KEY}&limit=100&code=505,513") // 던전 드랍, 카드 보상
            .retrieve()
            .body(TimelineResponseDto::class.java)
        return result!!
    }

}
