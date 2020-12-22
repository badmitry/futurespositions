package com.badmitry.futurespositions.mvp.presenter

import android.annotation.SuppressLint
import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.mvp.model.repo.IRetrofitFutureRepo
import com.badmitry.futurespositions.mvp.view.IGraphView
import com.badmitry.futurespositions.navigation.Screens
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GraphFuturesPresenter(private val future: Future) : MvpPresenter<IGraphView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var uiSchedulers: Scheduler

    @Inject
    lateinit var retrofitFutureRepo: IRetrofitFutureRepo

    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("SimpleDateFormat")
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val listDateForFuture = mutableListOf<String>()
        val listDateForStock = mutableListOf<String>()
        viewState.setFutureName(future.title)
        for (i in 1..365 step 15) {
            val newDate = Calendar.getInstance()
            newDate.add(Calendar.DATE, -i)
            listDateForFuture.add(SimpleDateFormat("dd.MM.yyyy").format(newDate.time))
            listDateForStock.add(SimpleDateFormat("yyyy-MM-dd").format(newDate.time))
        }

        retrofitFutureRepo.downloadListStockPrices(
            future.stockName,
            listDateForStock.last(),
            listDateForStock[0]
        )
            .observeOn(uiSchedulers).subscribe({ prices ->
                viewState.paintGraphStockPrice(prices)
            }, {
                println("Error: ${it.message}")
            }).addTo(compositeDisposable)

        retrofitFutureRepo.downloadListFuturesPositions(listDateForFuture, future.shortName)
            .observeOn(uiSchedulers).subscribe({ positions ->
                viewState.paintGraphsOfPositions(positions)
            }, {
                println("Error: ${it.message}")
            }).addTo(compositeDisposable)

    }

    fun displayGraphStockPrices() {
        viewState.displayGraphStockPrices()
    }

    fun displayGraphFuturesPositions() {
        viewState.displayGraphFuturesPositions()
    }

    fun backPressed(): Boolean {
        router.backTo(Screens.ChooseFutureScreen())
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}