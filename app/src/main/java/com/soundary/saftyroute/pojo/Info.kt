package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Info {
    @SerializedName("statuscode")
    @Expose
    var statuscode: Int? = null
    @SerializedName("copyright")
    @Expose
    var copyright: Copyright? = null
    @SerializedName("debug")
    @Expose
    var debug: String? = null
    @SerializedName("messages")
    @Expose
    var messages: List<Any>? = null

}