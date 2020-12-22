package com.badmitry.futurespositions.mvp.model.repo

import com.badmitry.futurespositions.mvp.model.entity.*
import com.badmitry.futurespositions.mvp.model.entity.responses.FuturePosition
import com.badmitry.futurespositions.mvp.model.entity.responses.SecuritiesResponse
import io.reactivex.rxjava3.core.Single

interface IRetrofitFutureRepo {
    fun downloadListFutures(): Single<List<Future>>
    fun downloadListStockPrices(stockName: String, from: String, till: String): Single<MutableList<Price>>
    fun downloadListFuturesPositions(dates: List<String>, futureShortName: String) : Single<List<FuturePosition>>
}