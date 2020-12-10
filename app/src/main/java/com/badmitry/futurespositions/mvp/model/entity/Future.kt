package com.badmitry.futurespositions.mvp.model.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Future(
    @Expose val orgId: String,
    @Expose val shortName: String,
    @Expose var title: String,
    @Expose var shortNameOfStock: String
) : Parcelable