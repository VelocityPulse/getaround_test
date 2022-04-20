package com.example.getaround.entities

import android.graphics.Bitmap
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("name") val name: String,
    @SerializedName("picture_url") val picture_url: String,
    @SerializedName("rating") val rating: Rating,
    @Nullable var ownerImage: Bitmap?,

    ) {
    constructor() : this(
        "",
        "",
        Rating(),
        null
    )
}
