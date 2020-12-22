package com.badmitry.futurespositions.mvp.model.cache.room.dao

import androidx.room.*
import com.badmitry.futurespositions.mvp.model.cache.room.RoomFuture

@Dao
interface FutureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: RoomFuture)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg repo: RoomFuture)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(repo: List<RoomFuture>)

    @Delete
    fun delete(repo: RoomFuture)

    @Delete
    fun delete(vararg repo: RoomFuture)

    @Delete
    fun delete(repo: List<RoomFuture>)

    @Update
    fun update(repo: RoomFuture)

    @Update
    fun update(vararg repo: RoomFuture)

    @Update
    fun update(repo: List<RoomFuture>)

    @Query("SELECT * FROM RoomFuture")
    fun getAll(): List<RoomFuture>

    @Query("SELECT * FROM RoomFuture WHERE isSelected = :isSelected")
    fun getSelected(isSelected: String): List<RoomFuture>
}