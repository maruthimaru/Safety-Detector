package com.soundary.saftyroute.retrofit;

import com.soundary.saftyroute.pojo.AlternateRoutesResponse;
import com.soundary.saftyroute.pojo.RequestLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetDataService {

    @POST("alternateroutes?key=42K0L9WTmAuyBS6L8jD8gadFz5KjW3Yu")
    Call<AlternateRoutesResponse> getAllPhotos(@Body RequestLocation requestLocation);
}
