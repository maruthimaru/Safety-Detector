package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Leg_ {
    @SerializedName("hasTollRoad")
    @Expose
    var hasTollRoad: Boolean? = null
    @SerializedName("hasBridge")
    @Expose
    var hasBridge: Boolean? = null
    @SerializedName("destNarrative")
    @Expose
    var destNarrative: String? = null
    @SerializedName("distance")
    @Expose
    var distance: Double? = null
    @SerializedName("hasTimedRestriction")
    @Expose
    var hasTimedRestriction: Boolean? = null
    @SerializedName("hasTunnel")
    @Expose
    var hasTunnel: Boolean? = null
    @SerializedName("hasHighway")
    @Expose
    var hasHighway: Boolean? = null
    @SerializedName("index")
    @Expose
    var index: Any? = null
    @SerializedName("formattedTime")
    @Expose
    var formattedTime: String? = null
    @SerializedName("origIndex")
    @Expose
    var origIndex: Any? = null
    @SerializedName("hasAccessRestriction")
    @Expose
    var hasAccessRestriction: Boolean? = null
    @SerializedName("hasSeasonalClosure")
    @Expose
    var hasSeasonalClosure: Boolean? = null
    @SerializedName("hasCountryCross")
    @Expose
    var hasCountryCross: Boolean? = null
    @SerializedName("roadGradeStrategy")
    @Expose
    var roadGradeStrategy: List<List<Any>>? = null
    @SerializedName("destIndex")
    @Expose
    var destIndex: Any? = null
    @SerializedName("time")
    @Expose
    var time: Any? = null
    @SerializedName("hasUnpaved")
    @Expose
    var hasUnpaved: Boolean? = null
    @SerializedName("origNarrative")
    @Expose
    var origNarrative: String? = null
    @SerializedName("maneuvers")
    @Expose
    var maneuvers: List<Maneuver_>? = null
    @SerializedName("hasFerry")
    @Expose
    var hasFerry: Boolean? = null

}