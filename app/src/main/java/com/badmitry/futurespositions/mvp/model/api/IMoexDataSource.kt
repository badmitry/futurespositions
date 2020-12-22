package com.badmitry.futurespositions.mvp.model.api

import com.badmitry.futurespositions.mvp.model.entity.responses.FuturePosition
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IMoexDataSource {
    @GET("/api/contract/OpenOptionService/{date}/F/{future}/json")
    fun getFuturePosition(
        @Path("date") date: String,
        @Path("future") future: String
    ): Single<List<FuturePosition>>
}