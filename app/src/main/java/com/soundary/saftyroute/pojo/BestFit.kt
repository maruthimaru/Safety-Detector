package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BestFit {
    @SerializedName("margin")
    @Expose
    var margin: Any? = null
    @SerializedName("newLevel")
    @Expose
    var newLevel: Any? = null
    @SerializedName("width")
    @Expose
    var width: Any? = null
    @SerializedName("scale")
    @Expose
    var scale: Any? = null
    @SerializedName("newScale")
    @Expose
    var newScale: Any? = null
    @SerializedName("newCenter")
    @Expose
    var newCenter: NewCenter? = null
    @SerializedName("height")
    @Expose
    var height: Any? = null

}