package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BoundingBox_ {
    @SerializedName("lr")
    @Expose
    var lr: Lr_? = null
    @SerializedName("ul")
    @Expose
    var ul: Ul_? = null

}