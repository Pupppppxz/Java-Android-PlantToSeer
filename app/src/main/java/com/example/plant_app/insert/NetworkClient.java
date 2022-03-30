package com.example.plant_app.insert;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public static Retrofit retrofit;
    private static String BASE_URL = "ggg";
    public static Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        }
        return retrofit;
    }
}
