package com.omarsahl.posts_core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.omarsahl.posts_core", appContext.getPackageName());
    }

    @Test
    public void test() {
        try {
            List<Post> posts = ApiServiceProvider.getInstance().getPostsForUser(1).execute().body();
            Log.d("ApiService", "posts=" + posts);
        } catch (IOException e) {
            Log.e("ApiService", "error retrieving posts", e);
        }
    }
}
