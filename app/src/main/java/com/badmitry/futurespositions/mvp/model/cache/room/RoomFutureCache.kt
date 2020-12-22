package com.badmitry.futurespositions.mvp.model.cache.room

import com.badmitry.futurespositions.mvp.model.cache.IFutureCache
import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.mvp.model.entity.responses.SecuritiesResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomFutureCache(private val db: Database) :
    IFutureCache {
    override fun loadInCache(futures: List<Future>) = Completable.fromAction {
        val future = futures.map {
            RoomFuture(
                it.emitentId,
                it.shortName,
                it.title,
                it.stockName,
                "0"
            )
        }
        db.futureDao.insert(future)
    }.subscribeOn(Schedulers.io())
//        val future = futures.map {
//            RoomFuture(
//                it.emitentId,
//                it.shortname.substring(0, 4),
//                it.emitentTitle,
//                it.shortNameOfStock ?: "",
//                "0"
//            )
//        }
//        db.futureDao.insert(future)
//    }.subscribeOn(Schedulers.io())

    override fun changeIsSelected(securitiesResponse: SecuritiesResponse, isSelected: String) = Completable.fromAction {
//        val newFuture = future.let {
//            RoomFuture(
//                it.emitentId,
//                it.shortname,
//                it.emitentTitle,
//                it.shortNameOfStock ?: "",
//                isSelected
//            )
//        }
//        db.futureDao.insert(newFuture)
    }.subscribeOn(Schedulers.io())

    override fun takeAllFromCache() = Single.fromCallable {
        db.futureDao.getAll().map { roomFuture ->
            Future(roomFuture.emitentId, roomFuture.shortname, roomFuture.emitentTitle, roomFuture.shortNameOfStock)
        }
    }.subscribeOn(Schedulers.io())

//    override fun takeSelectedFomCache() = Single.fromCallable {
//        db.futureDao.getSelected("1").map { roomFuture ->
//            Future(roomFuture.emitentId, roomFuture.shortname, roomFuture.emitentTitle, roomFuture.shortNameOfStock)
//        }
//    }

//    override fun loadUserInCache(users: List<GithubUser>) = Completable.fromAction {
//        val future = users.map {
//            RoomFuture(
//                "sdfdf",
//                "sdfsf",
//                "sdf",
//                "it.shortNameOfStock",
//                "0"
//            )
//        }
//        db.futureDao.insert(future)
//    }.subscribeOn(Schedulers.io())
}