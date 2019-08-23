package com.example.klsdinfo.endlessservice.api;

import com.example.klsdinfo.endlessservice.models.Rendezvous;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HorysAPI {

    @POST("rendezvous/")
    Call<Void> postRendezvous(@Body Rendezvous rendezvous);
}