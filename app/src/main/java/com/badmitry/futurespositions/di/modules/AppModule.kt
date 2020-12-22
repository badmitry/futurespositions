package com.badmitry.futurespositions.di.modules

import com.badmitry.futurespositions.ui.App
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@Module
class AppModule(private val app: App) {

    @Provides
    fun app() = app

    @Provides
    fun getUiSchelduler() = AndroidSchedulers.mainThread()
}