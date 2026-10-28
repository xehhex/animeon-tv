package fun.animeon.tv.data.repository

import fun.animeon.tv.data.api.ApiClient
import fun.animeon.tv.data.model.AnimeDetail
import fun.animeon.tv.data.model.AnimeItem
import fun.animeon.tv.data.model.ScheduleItem

class AnimeRepository {
    private val api = ApiClient.api

    suspend fun getPopularAnime(): List<AnimeItem> {
        return try {
            api.getAnimeList(page = 1, sort = "shikimori_rating").items
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getSchedule(): List<ScheduleItem> {
        return try {
            api.getSchedule()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAnimeDetail(animeUrl: String): Result<AnimeDetail> {
        return try {
            Result.success(api.getAnimeDetail(animeUrl))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchAnime(query: String): List<AnimeItem> {
        return try {
            api.getAnimeList(page = 1, search = query).items
        } catch (e: Exception) {
            emptyList()
        }
    }
}
