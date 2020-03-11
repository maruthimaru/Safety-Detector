package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StartPoint {
    @SerializedName("lng")
    @Expose
    var lng: Double? = null
    @SerializedName("lat")
    @Expose
    var lat: Double? = null

}