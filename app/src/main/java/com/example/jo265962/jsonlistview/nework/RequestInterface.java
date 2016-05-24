package com.example.jo265962.jsonlistview.nework;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by JO265962 on 5/21/2016.
 */
public interface RequestInterface {
    String BASE_URL = "https://dl.dropboxusercontent.com";

    @GET("/u/746330/facts.json")
    Call<JSONResponse> getJSON();
}
