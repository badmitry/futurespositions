package com.badmitry.futurespositions.di.modules

import com.badmitry.futurespositions.mvp.model.network.INetworkChecker
import com.badmitry.futurespositions.ui.App
import com.badmitry.futurespositions.ui.network.AndroidNetworkChecker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun networkStatus(app: App): INetworkChecker = AndroidNetworkChecker(app)
}