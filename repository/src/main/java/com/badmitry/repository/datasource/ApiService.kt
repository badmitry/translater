package com.badmitry.repository.datasource

import com.badmitry.data.DataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("words/search")
    suspend fun search(@Query("search") wordToSearch: String): List<DataModel>
}
