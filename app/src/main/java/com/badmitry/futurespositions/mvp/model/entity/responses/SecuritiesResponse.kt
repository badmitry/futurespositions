package com.badmitry.futurespositions.mvp.model.entity.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SecuritiesResponse(
    @Expose val securities: Data
) : Parcelable