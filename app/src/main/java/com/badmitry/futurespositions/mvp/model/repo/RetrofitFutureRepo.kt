package com.badmitry.futurespositions.mvp.model.repo

import android.annotation.SuppressLint
import com.badmitry.futurespositions.mvp.model.api.IIssDataSource
import com.badmitry.futurespositions.mvp.model.api.IMoexDataSource
import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.mvp.model.entity.Gdr
import com.badmitry.futurespositions.mvp.model.entity.Price
import com.badmitry.futurespositions.mvp.model.entity.Stock
import com.badmitry.futurespositions.mvp.model.entity.responses.FuturePosition
import com.badmitry.futurespositions.mvp.model.network.INetworkChecker
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class RetrofitFutureRepo(
    private val api: IIssDataSource,
    private val apiMoex: IMoexDataSource,
    private val networkChecker: INetworkChecker
) : IRetrofitFutureRepo {
    private fun getFutures(): Single<Set<Future>> {
        val list = mutableSetOf<Future>()
        return downloadFutures(list, 0)
    }

    private fun getStocks(): Single<List<Stock>> {
        val list = mutableListOf<Stock>()
        return downloadStocks(list, 0)
    }

    private fun getGdr(): Single<List<Gdr>> {
        val list = mutableListOf<Gdr>()
        return downloadGdr(list, 0)
    }

    private fun downloadFutures(list: MutableSet<Future>, startLine: Int): Single<Set<Future>> {
        return api.getFutures(startLine.toString()).flatMap { current ->
            if (current.securities.data.size == 0) {
                return@flatMap Single.just(list)
            } else {
                for (arr in current.securities.data) {
                    arr[7]?.let { list.add(Future(arr[7], arr[2].substring(0, 4))) }
                }
                val newStartLine = startLine + 100
                return@flatMap downloadFutures(
                    list.plus(list) as MutableSet<Future>,
                    newStartLine
                )
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun downloadStocks(list: MutableList<Stock>, startLine: Int): Single<List<Stock>> {
        return api.getStock(startLine.toString()).flatMap { current ->
            if (current.securities.data.size == 0) {
                return@flatMap Single.just(list)
            } else {
                for (arr in current.securities.data) {
                    arr[7]?.let { list.add(Stock(arr[7], arr[2], arr[1])) }
                }
                val newStartLine = startLine + 100
                return@flatMap downloadStocks(list.plus(list) as MutableList<Stock>, newStartLine)
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun downloadGdr(list: MutableList<Gdr>, startLine: Int): Single<List<Gdr>> {
        return api.getGdr(startLine.toString()).flatMap { current ->
            if (current.securities.data.size == 0) {
                return@flatMap Single.just(list)
            } else {
                for (arr in current.securities.data) {
                    arr[7]?.let { list.add(Gdr(arr[7], arr[2], arr[1])) }
                }
                val newStartLine = startLine + 100
                return@flatMap downloadGdr(list.plus(list) as MutableList<Gdr>, newStartLine)
            }
        }
    }

    override fun downloadListFutures(): Single<List<Future>> =
        networkChecker.isConnect().flatMap {
            if (it) {
                Single.zip(
                    getFutures(),
                    getStocks(),
                    getGdr(),
                    object : Function3<Set<Future>, List<Stock>, List<Gdr>, List<Future>> {
                        override fun invoke(
                            p1: Set<Future>,
                            p2: List<Stock>,
                            p3: List<Gdr>
                        ): List<Future> {
                            for (f in p1) {
                                if (f.emitentId == "687812") {
                                    p1.minus(f)
                                }
                                for (s in p2) {
                                    if (f.emitentId == s.emitentId) {
                                        f.title = s.title
                                        f.stockName = s.shortName
                                        break
                                    }
                                }
                                for (g in p3) {
                                    if (f.emitentId == g.emitentId && f.stockName == "") {
                                        f.title = g.title
                                        f.stockName = g.shortName
                                        break
                                    }
                                }
                            }
                            return p1.toList()
                        }
                    }
                ).subscribeOn(Schedulers.io())
            } else {
                Single.fromCallable { return@fromCallable mutableListOf<Future>() }
            }
        }.subscribeOn(Schedulers.io())

    @SuppressLint("SimpleDateFormat")
    override fun downloadListStockPrices(
        stockName: String,
        from: String,
        till: String
    ): Single<MutableList<Price>> =
        networkChecker.isConnect().flatMap {
            if (it) {
                api.getPriceStock(stockName, from, till).flatMap { current ->
                    val list = mutableListOf<Price>()
                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    for (arr in current.candles.data) {
                        list.add(Price(arr[1], formatter.parse(arr[6])))
                    }
                    Single.fromCallable { return@fromCallable list }
                }.subscribeOn(Schedulers.io())
            } else {
                Single.fromCallable { return@fromCallable mutableListOf<Price>() }
            }
        }.subscribeOn(Schedulers.io())

    override fun downloadListFuturesPositions(
        dates: List<String>,
        futureShortName: String
    ): Single<List<FuturePosition>> {
        val listSingle = mutableListOf<Single<FuturePosition>>()
        for (date in dates) {
            listSingle.add(apiMoex.getFuturePosition(date, futureShortName).flatMap {
                if (it.size > 0) {
                    Single.fromCallable { return@fromCallable it[0] }
                }else {
                    Single.fromCallable {return@fromCallable FuturePosition("01.01.1999",
                        "0",
                    "0",
                    "0",
                    "0")}
                }
            }.subscribeOn(Schedulers.io()))
        }
        return networkChecker.isConnect().flatMap {
            if (it) {
                Single.zip(
                    listSingle,
                    Function<Array<Any>, List<FuturePosition>> {
                        val listFutures = mutableListOf<FuturePosition>()
                        for (a in it) {
                            a as FuturePosition
                            listFutures.add(a)
                        }
                        val formatter = SimpleDateFormat("mm.DD.yyyy");
                        listFutures.sortBy { formatter.parse(it.Date) }
                        return@Function listFutures
                    }).subscribeOn(Schedulers.io())
            } else {
                Single.fromCallable { return@fromCallable mutableListOf<FuturePosition>() }
            }
        }
    }

//
//
//
//
//        networkChecker.isConnect().flatMap {
//            if (it) {
//                val listFuturePosition = mutableSetOf<FuturePosition>()
//                return@flatMap downloadPosition(listFuturePosition, dates.size -1, dates, futureShortName)
//            } else {
//                Single.fromCallable { return@fromCallable mutableListOf<FuturePosition>() }
//            }
//        }.subscribeOn(Schedulers.io())

//    private fun downloadPosition(
//        list: MutableSet<FuturePosition>,
//        counter: Int,
//        dates: List<String>,
//        futureShortName: String
//    ): Single<List<FuturePosition>> {
//        return apiMoex.getFuturePosition(dates[counter], futureShortName).flatMap { current ->
//            if (counter == 0) {
//                list.add(current[0])
//                val newList = list.toList()
//                return@flatMap Single.just(newList)
//            } else {
//                if (current.size == 0) {
//                    list.add(FuturePosition(dates[counter], "0", "0", "0", "0"))
//                } else {
//                    list.add(current[0])
//                }
//                val newCounter = counter - 1
//                return@flatMap downloadPosition(
//                    list.plus(list) as MutableSet<FuturePosition>,
//                    newCounter,
//                    dates,
//                    futureShortName
//                )
//            }
//        }
//    }
}