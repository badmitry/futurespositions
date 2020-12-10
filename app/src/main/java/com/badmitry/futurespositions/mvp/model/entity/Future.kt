package com.badmitry.futurespositions.mvp.model.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Future(
    @Expose val orgId: String,
    @Expose val shortName: String,
    @Expose val title: String,
    @Expose val shortNameOfStock: String
) : Parcelable