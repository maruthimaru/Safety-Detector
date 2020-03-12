package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Shape_ {
    @SerializedName("legIndexes")
    @Expose
    var legIndexes: List<Any>? = null
    @SerializedName("maneuverIndexes")
    @Expose
    var maneuverIndexes: List<Any>? = null
    @SerializedName("shapePoints")
    @Expose
    var shapePoints: List<Double>? = null

}