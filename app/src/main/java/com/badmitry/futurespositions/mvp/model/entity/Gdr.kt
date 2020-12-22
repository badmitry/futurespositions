package com.badmitry.futurespositions.mvp.model.entity

import com.google.gson.annotations.Expose

data class Gdr(
    @Expose val emitentId: String,
    @Expose val title: String,
    @Expose val shortName: String
)