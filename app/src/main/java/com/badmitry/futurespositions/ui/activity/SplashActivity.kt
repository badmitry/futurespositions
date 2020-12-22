package com.badmitry.futurespositions.ui.activity

import android.os.Bundle
import com.badmitry.futurespositions.mvp.presenter.SplashPresenter
import com.badmitry.futurespositions.mvp.view.ISplashView
import com.badmitry.futurespositions.ui.App
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class SplashActivity : MvpAppCompatActivity(), ISplashView {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val presenter by moxyPresenter {
        SplashPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        val navigator = SupportAppNavigator(this, 0)
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun finishSplash() {
        this.finish()
    }

}