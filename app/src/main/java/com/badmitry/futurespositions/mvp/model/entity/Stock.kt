package com.badmitry.futurespositions.mvp.model.entity

import com.google.gson.annotations.Expose

data class Stock(
    @Expose val emitentId: String,
    @Expose val title: String,
    @Expose val shortName: String
)