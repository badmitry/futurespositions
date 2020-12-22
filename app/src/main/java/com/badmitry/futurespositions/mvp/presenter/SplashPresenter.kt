package com.badmitry.futurespositions.mvp.presenter

import com.badmitry.futurespositions.mvp.model.cache.IFutureCache
import com.badmitry.futurespositions.mvp.model.repo.IRetrofitFutureRepo
import com.badmitry.futurespositions.mvp.view.ISplashView
import com.badmitry.futurespositions.navigation.Screens
import com.badmitry.futurespositions.ui.App
import io.reactivex.rxjava3.core.Scheduler
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SplashPresenter : MvpPresenter<ISplashView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var uiSchedulers: Scheduler

    @Inject
    lateinit var retrofitFutureRepo: IRetrofitFutureRepo

    @Inject
    lateinit var roomFutureCache: IFutureCache

    @Inject
    lateinit var cache: IFutureCache

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        App.component.inject(this)
        retrofitFutureRepo.downloadListFutures().observeOn(uiSchedulers).subscribe({ futures ->
            if (futures.isNotEmpty()) {
                cache.loadInCache(futures).observeOn(uiSchedulers).subscribe {
                    router.navigateTo(Screens.MainActivityScreen(true))
                    viewState.finishSplash()
                }
            } else {
                router.navigateTo(Screens.MainActivityScreen(false))
                viewState.finishSplash()
            }

        }, {
            println("Error: ${it.message}")
            router.navigateTo(Screens.MainActivityScreen(false))
            viewState.finishSplash()
        })
    }
}