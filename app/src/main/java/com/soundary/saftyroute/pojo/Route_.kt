package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Route_ {
    @SerializedName("boundingBox")
    @Expose
    var boundingBox: BoundingBox_? = null
    @SerializedName("distance")
    @Expose
    var distance: Double? = null
    @SerializedName("hasTimedRestriction")
    @Expose
    var hasTimedRestriction: Boolean? = null
    @SerializedName("computedWaypoAnys")
    @Expose
    var computedWaypoints: List<Any>? = null
    @SerializedName("routeError")
    @Expose
    var routeError: RouteError_? = null
    @SerializedName("realTimeAlternatePercentSavingsThreshhold")
    @Expose
    var realTimeAlternatePercentSavingsThreshhold: String? = null
    @SerializedName("formattedTime")
    @Expose
    var formattedTime: String? = null
    @SerializedName("bestFit")
    @Expose
    var bestFit: BestFit_? = null
    @SerializedName("realTimeAlternateTimeSavingsThreshhold")
    @Expose
    var realTimeAlternateTimeSavingsThreshhold: String? = null
    @SerializedName("hasAccessRestriction")
    @Expose
    var hasAccessRestriction: Boolean? = null
    @SerializedName("hasSeasonalClosure")
    @Expose
    var hasSeasonalClosure: Boolean? = null
    @SerializedName("hasCountryCross")
    @Expose
    var hasCountryCross: Boolean? = null
    @SerializedName("legs")
    @Expose
    var legs: List<Leg_>? = null
    @SerializedName("options")
    @Expose
    var options: Options_? = null
    @SerializedName("hasFerry")
    @Expose
    var hasFerry: Boolean? = null
    @SerializedName("hasTollRoad")
    @Expose
    var hasTollRoad: Boolean? = null
    @SerializedName("hasBridge")
    @Expose
    var hasBridge: Boolean? = null
    @SerializedName("shape")
    @Expose
    var shape: Shape_? = null
    @SerializedName("hasTunnel")
    @Expose
    var hasTunnel: Boolean? = null
    @SerializedName("hasHighway")
    @Expose
    var hasHighway: Boolean? = null
    @SerializedName("sessionId")
    @Expose
    var sessionId: String? = null
    @SerializedName("maxRoutes")
    @Expose
    var maxRoutes: String? = null
    @SerializedName("realTime")
    @Expose
    var realTime: Any? = null
    @SerializedName("fuelUsed")
    @Expose
    var fuelUsed: Double? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("timeOverage")
    @Expose
    var timeOverage: Double? = null
    @SerializedName("locations")
    @Expose
    var locations: List<Location>? = null
    @SerializedName("time")
    @Expose
    var time: Any? = null
    @SerializedName("hasUnpaved")
    @Expose
    var hasUnpaved: Boolean? = null
    @SerializedName("locationSequence")
    @Expose
    var locationSequence: List<Any>? = null

}