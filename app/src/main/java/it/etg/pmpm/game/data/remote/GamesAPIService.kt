package it.etg.pmpm.game.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface GamesAPIService {
    @GET("games")
    suspend fun getGames(): Response<List<GamesResponse>>
}