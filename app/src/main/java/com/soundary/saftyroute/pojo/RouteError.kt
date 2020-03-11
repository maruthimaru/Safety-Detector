package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RouteError {
    @SerializedName("errorCode")
    @Expose
    var errorCode: Any? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

}