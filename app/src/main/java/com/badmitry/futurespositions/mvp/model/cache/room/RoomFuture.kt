package com.badmitry.futurespositions.mvp.model.cache.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomFuture (
    @PrimaryKey var emitentId: String,
    var shortname: String,
    var emitentTitle: String,
    var shortNameOfStock: String,
    var isSelected: String
)