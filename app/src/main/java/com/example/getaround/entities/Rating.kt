package com.example.getaround.entities

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("average") val average: Float,
    @SerializedName("count") val count: Int,
) {

    constructor() : this(
        0f,
        0
    )
}
