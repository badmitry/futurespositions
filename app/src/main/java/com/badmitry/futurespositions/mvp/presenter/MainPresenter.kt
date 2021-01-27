package com.badmitry.futurespositions.mvp.presenter

import com.badmitry.futurespositions.mvp.view.IMainView
import com.badmitry.futurespositions.navigation.Screens
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainPresenter(val isConnecting: Boolean): MvpPresenter<IMainView>() {

    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (isConnecting) {
            router.replaceScreen(Screens.ChooseFutureScreen())
        } else {
            router.replaceScreen(Screens.NetReconnectionScreen())
        }
    }

    fun backClick() {
        router.exit()
    }

}