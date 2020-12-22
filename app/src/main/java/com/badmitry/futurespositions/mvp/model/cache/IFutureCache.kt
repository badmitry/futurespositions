package com.badmitry.futurespositions.mvp.model.cache

import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.mvp.model.entity.responses.SecuritiesResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IFutureCache {
    fun loadInCache(futures: List<Future>): Completable
    fun changeIsSelected(securitiesResponse: SecuritiesResponse, isSelected: String): Completable
    fun takeAllFromCache(): Single<List<Future>>
}