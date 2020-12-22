package com.badmitry.futurespositions.mvp.model.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.badmitry.futurespositions.mvp.model.cache.room.dao.FutureDao

@Database(entities = [RoomFuture::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract val futureDao: FutureDao

    companion object {
        const val DB_NAME = "database.db"
        private var instance: com.badmitry.futurespositions.mvp.model.cache.room.Database? = null
        fun create(context: Context) {
            instance ?: let {
                instance = Room.databaseBuilder(context, com.badmitry.futurespositions.mvp.model.cache.room.Database::class.java, DB_NAME)
                    .build()
            }
        }
    }
}