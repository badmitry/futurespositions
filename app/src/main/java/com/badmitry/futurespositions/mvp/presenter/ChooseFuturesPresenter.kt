package com.badmitry.futurespositions.mvp.presenter

import com.badmitry.futurespositions.mvp.model.cache.IFutureCache
import com.badmitry.futurespositions.mvp.model.entity.Future
import com.badmitry.futurespositions.mvp.model.repo.IRetrofitFutureRepo
import com.badmitry.futurespositions.mvp.presenter.list.IFutureListPresenter
import com.badmitry.futurespositions.mvp.view.IChooseFuturesView
import com.badmitry.futurespositions.mvp.view.INetReconnectView
import com.badmitry.futurespositions.mvp.view.list.IFutureItemView
import com.badmitry.futurespositions.navigation.Screens
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class ChooseFuturesPresenter : MvpPresenter<IChooseFuturesView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var uiSchedulers: Scheduler

    @Inject
    lateinit var cache: IFutureCache

    class FutureListPresenter : IFutureListPresenter {
        var futures = mutableListOf<Future>()
        override var itemClickListener: ((IFutureItemView) -> Unit)? = null
        override fun bindView(view: IFutureItemView) {
            val future = futures[view.pos]
            view.setName(future.title)
        }
        override fun getCount() = futures.size
    }

    val futureListPresenter = FutureListPresenter()
    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()
        futureListPresenter.itemClickListener = { itemView ->
            router.navigateTo(Screens.GraphFutureScreen(futureListPresenter.futures[itemView.pos]))
        }
    }

    private fun loadData() {
        cache.takeAllFromCache().observeOn(uiSchedulers).subscribe({repos ->
            futureListPresenter.futures.clear()
            futureListPresenter.futures.addAll(repos)
            viewState.updateList()
        },{
            println("Error: ${it.message}")
        }).addTo(compositeDisposable)
    }


    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}