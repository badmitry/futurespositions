package com.badmitry.futurespositions.di

import com.badmitry.futurespositions.di.modules.*
import com.badmitry.futurespositions.mvp.presenter.*
import com.badmitry.futurespositions.ui.activity.MainActivity
import com.badmitry.futurespositions.ui.activity.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        AppModule::class,
        CacheModule::class,
        NavigationModule::class,
        RepoModule::class
    ]
)
interface AppComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(splashPresenter: SplashPresenter)
    fun inject(mainPresenter: MainPresenter)
    fun inject(mainActivity: MainActivity)
    fun inject(netReconnectionPresenter: NetReconnectionPresenter)
    fun inject(chooseFuturesPresenter: ChooseFuturesPresenter)
    fun inject(graphFuturesPresenter: GraphFuturesPresenter)
}