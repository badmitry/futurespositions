package com.badmitry.futurespositions.di.modules

import com.badmitry.futurespositions.mvp.model.api.IIssDataSource
import com.badmitry.futurespositions.mvp.model.api.IMoexDataSource
import com.badmitry.futurespositions.mvp.model.network.INetworkChecker
import com.badmitry.futurespositions.mvp.model.repo.IRetrofitFutureRepo
import com.badmitry.futurespositions.mvp.model.repo.RetrofitFutureRepo
import com.badmitry.futurespositions.ui.App
import com.badmitry.futurespositions.ui.network.AndroidNetworkChecker
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepoModule {
    @Singleton
    @Provides
    fun networkStatus(app: App): INetworkChecker = AndroidNetworkChecker(app)

    @Singleton
    @Provides
    fun getFutureRepo(
        @Named("iss") api: IIssDataSource,
        @Named("moex") apiMoex: IMoexDataSource,
        networkChecker: INetworkChecker
    ): IRetrofitFutureRepo = RetrofitFutureRepo(api, apiMoex, networkChecker)

}