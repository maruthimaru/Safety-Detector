package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlternateRoute {
    @SerializedName("route")
    @Expose
    var route: Route_? = null
    @SerializedName("info")
    @Expose
    var info: Info? = null

}