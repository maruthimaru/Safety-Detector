package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Copyright_ {
    @SerializedName("imageAltText")
    @Expose
    var imageAltText: String? = null
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null

}