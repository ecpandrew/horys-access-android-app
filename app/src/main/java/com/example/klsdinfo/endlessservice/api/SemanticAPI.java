package com.example.klsdinfo.endlessservice.api;

import java.util.List;

import com.example.klsdinfo.endlessservice.models.Device;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SemanticAPI {
    @GET("things/")
    Call<List<Device>> getDevices();

    @GET("persons/{email}/mhubs")
    Call<List<Device>> getUserDevices(@Path("email") String email);
}