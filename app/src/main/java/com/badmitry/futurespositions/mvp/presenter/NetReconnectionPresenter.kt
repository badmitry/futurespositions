package com.badmitry.futurespositions.mvp.presenter

import com.badmitry.futurespositions.mvp.model.repo.IRetrofitFutureRepo
import com.badmitry.futurespositions.mvp.view.INetReconnectView
import com.badmitry.futurespositions.navigation.Screens
import io.reactivex.rxjava3.core.Scheduler
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class NetReconnectionPresenter() : MvpPresenter<INetReconnectView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var uiSchedulers: Scheduler

    @Inject
    lateinit var retrofitFutureRepo: IRetrofitFutureRepo

    fun reloadFutures() {
        retrofitFutureRepo.downloadListFutures().observeOn(uiSchedulers).subscribe({
            if (it.isEmpty()) {
                router.replaceScreen(Screens.NetReconnectionScreen())
            } else {
                router.replaceScreen(Screens.ChooseFutureScreen())
            }
        }, {
            println("Error: ${it.message}")
            router.replaceScreen(Screens.NetReconnectionScreen())
        })
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}