package com.omarsahl.posts_core;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/users")
    Observable<List<User>> getUsers();

    @GET("/posts")
    Observable<List<Post>> getPostsForUser(@Query("userId") int userId);
}
