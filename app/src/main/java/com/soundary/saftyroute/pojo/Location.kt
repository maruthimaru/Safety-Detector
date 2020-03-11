package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {
    @SerializedName("dragPoint")
    @Expose
    var dragPoint: Boolean? = null
    @SerializedName("displayLatLng")
    @Expose
    var displayLatLng: DisplayLatLng? = null
    @SerializedName("adminArea4")
    @Expose
    var adminArea4: String? = null
    @SerializedName("adminArea5")
    @Expose
    var adminArea5: String? = null
    @SerializedName("postalCode")
    @Expose
    var postalCode: String? = null
    @SerializedName("adminArea1")
    @Expose
    var adminArea1: String? = null
    @SerializedName("adminArea3")
    @Expose
    var adminArea3: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("sideOfStreet")
    @Expose
    var sideOfStreet: String? = null
    @SerializedName("geocodeQualityCode")
    @Expose
    var geocodeQualityCode: String? = null
    @SerializedName("adminArea4Type")
    @Expose
    var adminArea4Type: String? = null
    @SerializedName("linkId")
    @Expose
    var linkId: Any? = null
    @SerializedName("street")
    @Expose
    var street: String? = null
    @SerializedName("adminArea5Type")
    @Expose
    var adminArea5Type: String? = null
    @SerializedName("geocodeQuality")
    @Expose
    var geocodeQuality: String? = null
    @SerializedName("adminArea1Type")
    @Expose
    var adminArea1Type: String? = null
    @SerializedName("adminArea3Type")
    @Expose
    var adminArea3Type: String? = null
    @SerializedName("latLng")
    @Expose
    var latLng: LatLng? = null

}