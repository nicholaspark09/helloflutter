package com.omarsahl.posts_core;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceProvider {

    private ApiServiceProvider() {
    }

    private static volatile ApiService INSTANCE = null;

    public static ApiService getInstance() {
        if (INSTANCE == null) {
            synchronized (ApiServiceProvider.class) {
                Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                INSTANCE = retrofit.create(ApiService.class);
            }
        }

        return INSTANCE;
    }
}
