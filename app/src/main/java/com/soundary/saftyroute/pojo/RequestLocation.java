
package com.soundary.saftyroute.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLocation {

    @SerializedName("locations")
    @Expose
    private List<String> locations = null;
    @SerializedName("maxRoutes")
    @Expose
    private Integer maxRoutes;
    @SerializedName("timeOverage")
    @Expose
    private Integer timeOverage;

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Integer getMaxRoutes() {
        return maxRoutes;
    }

    public void setMaxRoutes(Integer maxRoutes) {
        this.maxRoutes = maxRoutes;
    }

    public Integer getTimeOverage() {
        return timeOverage;
    }

    public void setTimeOverage(Integer timeOverage) {
        this.timeOverage = timeOverage;
    }

    public RequestLocation(List<String> locations, Integer maxRoutes, Integer timeOverage) {
        this.locations = locations;
        this.maxRoutes = maxRoutes;
        this.timeOverage = timeOverage;
    }
}
