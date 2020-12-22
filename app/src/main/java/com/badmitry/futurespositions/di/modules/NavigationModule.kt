package com.badmitry.futurespositions.di.modules

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

@Module
class NavigationModule {
    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    fun getCicerone() = cicerone

    @Provides
    fun getRouter() = cicerone.router

    @Provides
    fun getNavigatorHolder() = cicerone.navigatorHolder
}