package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MapState {
    @SerializedName("center")
    @Expose
    var center: Center? = null
    @SerializedName("width")
    @Expose
    var width: Any? = null
    @SerializedName("scale")
    @Expose
    var scale: Any? = null
    @SerializedName("height")
    @Expose
    var height: Any? = null

}