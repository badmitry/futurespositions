package com.badmitry.futurespositions.di.modules

import androidx.room.Room
import com.badmitry.futurespositions.mvp.model.cache.IFutureCache
import com.badmitry.futurespositions.mvp.model.cache.room.Database
import com.badmitry.futurespositions.mvp.model.cache.room.RoomFutureCache
import com.badmitry.futurespositions.ui.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {
    @Singleton
    @Provides
    fun getDatabase(app: App) =
        Room.databaseBuilder(
            app,
            Database::class.java,
            Database.DB_NAME
        )
            .build()

    @Singleton
    @Provides
    fun getFutureCache(database: Database): IFutureCache = RoomFutureCache(database)
}