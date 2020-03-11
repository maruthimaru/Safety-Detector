package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Options {
    @SerializedName("arteryWeights")
    @Expose
    var arteryWeights: List<Any>? = null
    @SerializedName("cyclingRoadFactor")
    @Expose
    var cyclingRoadFactor: Any? = null
    @SerializedName("timeType")
    @Expose
    var timeType: Any? = null
    @SerializedName("useTraffic")
    @Expose
    var useTraffic: Boolean? = null
    @SerializedName("returnLinkDirections")
    @Expose
    var returnLinkDirections: Boolean? = null
    @SerializedName("countryBoundaryDisplay")
    @Expose
    var countryBoundaryDisplay: Boolean? = null
    @SerializedName("enhancedNarrative")
    @Expose
    var enhancedNarrative: Boolean? = null
    @SerializedName("locale")
    @Expose
    var locale: String? = null
    @SerializedName("tryAvoidLinkIds")
    @Expose
    var tryAvoidLinkIds: List<Any>? = null
    @SerializedName("drivingStyle")
    @Expose
    var drivingStyle: Any? = null
    @SerializedName("doReverseGeocode")
    @Expose
    var doReverseGeocode: Boolean? = null
    @SerializedName("generalize")
    @Expose
    var generalize: Any? = null
    @SerializedName("mustAvoidLinkIds")
    @Expose
    var mustAvoidLinkIds: List<Any>? = null
    @SerializedName("sideOfStreetDisplay")
    @Expose
    var sideOfStreetDisplay: Boolean? = null
    @SerializedName("routeType")
    @Expose
    var routeType: String? = null
    @SerializedName("avoidTimedConditions")
    @Expose
    var avoidTimedConditions: Boolean? = null
    @SerializedName("routeNumber")
    @Expose
    var routeNumber: Any? = null
    @SerializedName("shapeFormat")
    @Expose
    var shapeFormat: String? = null
    @SerializedName("maxWalkingDistance")
    @Expose
    var maxWalkingDistance: Any? = null
    @SerializedName("destinationManeuverDisplay")
    @Expose
    var destinationManeuverDisplay: Boolean? = null
    @SerializedName("transferPenalty")
    @Expose
    var transferPenalty: Any? = null
    @SerializedName("narrativeType")
    @Expose
    var narrativeType: String? = null
    @SerializedName("walkingSpeed")
    @Expose
    var walkingSpeed: Any? = null
    @SerializedName("urbanAvoidFactor")
    @Expose
    var urbanAvoidFactor: Any? = null
    @SerializedName("stateBoundaryDisplay")
    @Expose
    var stateBoundaryDisplay: Boolean? = null
    @SerializedName("unit")
    @Expose
    var unit: String? = null
    @SerializedName("highwayEfficiency")
    @Expose
    var highwayEfficiency: Any? = null
    @SerializedName("maxLinkId")
    @Expose
    var maxLinkId: Any? = null
    @SerializedName("maneuverPenalty")
    @Expose
    var maneuverPenalty: Any? = null
    @SerializedName("avoidTripIds")
    @Expose
    var avoidTripIds: List<Any>? = null
    @SerializedName("filterZoneFactor")
    @Expose
    var filterZoneFactor: Any? = null
    @SerializedName("manmaps")
    @Expose
    var manmaps: String? = null

}