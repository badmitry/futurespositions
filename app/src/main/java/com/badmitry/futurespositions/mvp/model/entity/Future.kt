package com.badmitry.futurespositions.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Future(
    val emitentId: String,
    val shortName: String,
    var title: String = "",
    var stockName: String = ""
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        when (other) {
            is Future -> {
                return this.emitentId == other.emitentId && this.shortName == other.shortName
            }
            else -> return false
        }
    }
}