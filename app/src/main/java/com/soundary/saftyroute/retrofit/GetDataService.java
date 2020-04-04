package com.soundary.saftyroute.retrofit;

import com.soundary.saftyroute.pojo.AlternateRoutesResponse;
import com.soundary.saftyroute.pojo.RequestLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetDataService {

    @POST("alternateroutes?key=42K0L9WTmAuyBS6L8jD8gadFz5KjW3Yu")
    Call<AlternateRoutesResponse> getAllPhotos(@Body RequestLocation requestLocation);

    @GET("alternateroutes")
    Call<AlternateRoutesResponse> getRoute(@Query(value ="key")String user,
                                           @Query(value ="from")String from,
    @Query(value ="to")String to,
                                                   @Query(value ="outFormat")String outFormat,
                                           @Query(value ="ambiguities")String ambiguities,
                                           @Query(value ="routeType")String routeType,
                                           @Query(value ="maxRoutes")int maxRoutes,
                                           @Query(value ="timeOverage")int timeOverage,
                                           @Query(value ="doReverseGeocode")boolean doReverseGeocode,
                                           @Query(value ="enhancedNarrative")boolean enhancedNarrative,
                                           @Query(value ="avoidTimedConditions")boolean avoidTimedConditions,
                                           @Query(value ="unit")String unit);
}
