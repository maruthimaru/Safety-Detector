package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Maneuver_ {
    @SerializedName("distance")
    @Expose
    var distance: Any? = null
    @SerializedName("streets")
    @Expose
    var streets: List<String>? = null
    @SerializedName("narrative")
    @Expose
    var narrative: String? = null
    @SerializedName("turnType")
    @Expose
    var turnType: Any? = null
    @SerializedName("startPoint")
    @Expose
    var startPoint: StartPoint_? = null
    @SerializedName("index")
    @Expose
    var index: Any? = null
    @SerializedName("formattedTime")
    @Expose
    var formattedTime: String? = null
    @SerializedName("directionName")
    @Expose
    var directionName: String? = null
    @SerializedName("maneuverNotes")
    @Expose
    var maneuverNotes: List<Any>? = null
    @SerializedName("linkIds")
    @Expose
    var linkIds: List<Any>? = null
    @SerializedName("signs")
    @Expose
    var signs: List<Any>? = null
    @SerializedName("mapUrl")
    @Expose
    var mapUrl: String? = null
    @SerializedName("transportMode")
    @Expose
    var transportMode: String? = null
    @SerializedName("attributes")
    @Expose
    var attributes: Any? = null
    @SerializedName("time")
    @Expose
    var time: Any? = null
    @SerializedName("iconUrl")
    @Expose
    var iconUrl: String? = null
    @SerializedName("direction")
    @Expose
    var direction: Any? = null

}