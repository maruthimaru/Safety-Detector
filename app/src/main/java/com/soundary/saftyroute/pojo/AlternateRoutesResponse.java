
package com.soundary.saftyroute.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlternateRoutesResponse {

    @SerializedName("route")
    @Expose
    private Route route;
    @SerializedName("info")
    @Expose
    private Info_ info;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Info_ getInfo() {
        return info;
    }

    public void setInfo(Info_ info) {
        this.info = info;
    }

}
