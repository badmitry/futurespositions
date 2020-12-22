package com.badmitry.futurespositions.mvp.model.entity.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FuturePosition(
    @Expose val Date: String,
    @Expose val JuridicalLong: String,
    @Expose val JuridicalShort: String,
    @Expose val PhysicalLong: String,
    @Expose val PhysicalShort: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        when (other) {
            is FuturePosition -> {
                return this.Date == other.Date
            }
            else -> return false
        }
    }
}
