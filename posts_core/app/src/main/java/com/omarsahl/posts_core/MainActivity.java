package com.omarsahl.posts_core;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends FlutterActivity {

    private static final String TAG = "MainActivity";
    private CompositeDisposable disposables = new CompositeDisposable();
    private Gson gson = new GsonBuilder().serializeNulls().create();

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        new MethodChannel(getFlutterView(), "posts_demo/api_channel")
            .setMethodCallHandler((methodCall, result) -> {
                if (methodCall.method.equals("getAllUsers")) {
                    getAllUsers(result);
                } else if (methodCall.method.equals("getPostsForUser")) {
                    if (methodCall.hasArgument("userId")) {
                        getPostsForUser(methodCall.argument("userId"), result);
                    } else {
                        result.error("error", "userId must be passed as an arg", null);
                    }
                } else {
                    result.notImplemented();
                }
            });
    }

    private void getAllUsers(MethodChannel.Result result) {
        Disposable disposable = ApiServiceProvider.getInstance()
            .getUsers()
            .subscribeOn(Schedulers.io())
            .map(users -> {
                List<String> jsonList = new ArrayList<>();
                for (User u : users) {
                    jsonList.add(gson.toJson(u));
                }
                return jsonList;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                result::success,
                throwable -> Log.e(TAG, "getAllUsers error", throwable)
            );

        disposables.add(disposable);
    }

    private void getPostsForUser(int userId, MethodChannel.Result result) {
        Disposable disposable = ApiServiceProvider.getInstance()
            .getPostsForUser(userId)
            .subscribeOn(Schedulers.io())
            .map(posts -> {
                List<String> jsonList = new ArrayList<>();
                for (Post p : posts) {
                    jsonList.add(gson.toJson(p));
                }
                return jsonList;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                result::success,
                throwable -> Log.e(TAG, "getPostsForUser error", throwable)
            );

        disposables.add(disposable);
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}
