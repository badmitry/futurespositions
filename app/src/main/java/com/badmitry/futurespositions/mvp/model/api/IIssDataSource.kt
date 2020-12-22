package com.badmitry.futurespositions.mvp.model.api

import com.badmitry.futurespositions.mvp.model.entity.responses.SecuritiesResponse
import com.badmitry.futurespositions.mvp.model.entity.responses.CandlesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IIssDataSource {
    @GET("/iss/securities.json?is_trading=1&group_by=type&group_by_filter=futures")
    fun getFutures(@Query("start") startLine: String): Single<SecuritiesResponse>

    @GET("/iss/securities.json?is_trading=1&group_by=group&group_by_filter=stock_shares")
    fun getStock(@Query("start") startLine: String): Single<SecuritiesResponse>

    @GET("/iss/securities.json?is_trading=1&group_by=group&group_by_filter=stock_dr")
    fun getGdr(@Query("start") startLine: String): Single<SecuritiesResponse>

    @GET("/iss/engines/stock/markets/shares/boards/TQBR/securities/{stock}/candles.json")
    fun getPriceStock(
        @Path("stock") stock: String,
        @Query("from") from: String,
        @Query("till") till: String,
        @Query("interval") interval: String = "24"
    ) : Single<CandlesResponse>
}