package com.animeon.tv.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeResponse(
    @SerialName("items") val items: List<AnimeItem> = emptyList(),
    @SerialName("page") val page: Int = 1,
    @SerialName("total_count") val totalCount: Int = 0,
    @SerialName("total_pages") val totalPages: Int = 1
)

@Serializable
data class AnimeItem(
    @SerialName("id") val id: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("title_orig") val titleOrig: String? = null,
    @SerialName("anime_url") val animeUrl: String = "",
    @SerialName("poster") val poster: String? = null,
    @SerialName("anime_poster") val animePoster: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("rating") val rating: Double? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("material_data") val materialData: MaterialData? = null
) {
    val displayPosterUrl: String
        get() {
            if (!materialData?.posterUrl.isNullOrEmpty()) return materialData!!.posterUrl!!
            val raw = poster ?: animePoster ?: ""
            return if (raw.startsWith("http")) raw else "https://api.animeon.cloud$raw"
        }

    val displayGenres: String
        get() = materialData?.animeGenres?.joinToString(", ") ?: ""
}

@Serializable
data class MaterialData(
    @SerialName("anime_title") val animeTitle: String? = null,
    @SerialName("anime_genres") val animeGenres: List<String>? = null,
    @SerialName("poster_url") val posterUrl: String? = null,
    @SerialName("episodes_aired") val episodesAired: Int? = null,
    @SerialName("episodes_total") val episodesTotal: Int? = null
)

@Serializable
data class ScheduleItem(
    @SerialName("id") val id: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("title_orig") val titleOrig: String? = null,
    @SerialName("anime_url") val animeUrl: String = "",
    @SerialName("poster_url") val posterUrl: String? = null,
    @SerialName("episodes_aired") val episodesAired: Int? = null,
    @SerialName("episodes_total") val episodesTotal: Int? = null,
    @SerialName("next_episode") val nextEpisode: Int? = null,
    @SerialName("next_episode_at") val nextEpisodeAt: String? = null,
    @SerialName("rating") val rating: Double? = null
)

@Serializable
data class AnimeDetail(
    @SerialName("id") val id: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("title_orig") val titleOrig: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("poster") val poster: String? = null,
    @SerialName("anime_poster") val animePoster: String? = null,
    @SerialName("genres") val genres: List<String>? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("alloha_iframe") val allohaIframe: String? = null,
    @SerialName("translations") val translations: Map<String, TranslationInfo>? = null,
    @SerialName("material_data") val materialData: MaterialData? = null
) {
    val displayPosterUrl: String
        get() {
            if (!materialData?.posterUrl.isNullOrEmpty()) return materialData!!.posterUrl!!
            val raw = poster ?: animePoster ?: ""
            return if (raw.startsWith("http")) raw else "https://api.animeon.cloud$raw"
        }
}

@Serializable
data class TranslationInfo(
    @SerialName("episodes") val episodes: Map<String, EpisodeInfo> = emptyMap(),
    @SerialName("is_active") val isActive: Boolean? = true,
    @SerialName("season") val season: Int? = 1
)

@Serializable
data class EpisodeInfo(
    @SerialName("link") val link: String? = null,
    @SerialName("screenshots") val screenshots: List<String>? = null
) {
    val normalizedLink: String
        get() {
            val l = link ?: ""
            return if (l.startsWith("//")) "https:$l" else l
        }
}
