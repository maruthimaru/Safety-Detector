package com.soundary.saftyroute.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BoundingBox {
    @SerializedName("lr")
    @Expose
    var lr: Lr? = null
    @SerializedName("ul")
    @Expose
    var ul: Ul? = null

}