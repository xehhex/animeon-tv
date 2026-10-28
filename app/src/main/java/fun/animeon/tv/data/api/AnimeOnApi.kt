package fun.animeon.tv.data.api

import fun.animeon.tv.data.model.AnimeDetail
import fun.animeon.tv.data.model.AnimeResponse
import fun.animeon.tv.data.model.ScheduleItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeOnApi {

    @GET("api/anime")
    suspend fun getAnimeList(
        @Query("page") page: Int = 1,
        @Query("status") status: String? = null,
        @Query("kind") kind: String? = null,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = "shikimori_rating"
    ): AnimeResponse

    @GET("api/anime/schedule")
    suspend fun getSchedule(): List<ScheduleItem>

    @GET("api/anime/{animeUrl}")
    suspend fun getAnimeDetail(
        @Path("animeUrl") animeUrl: String
    ): AnimeDetail
}
