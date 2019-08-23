package com.example.klsdinfo.endlessservice.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SemanticClient {
    private static String BASE_URL = "http://smartlab.lsdi.ufma.br/semantic/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}