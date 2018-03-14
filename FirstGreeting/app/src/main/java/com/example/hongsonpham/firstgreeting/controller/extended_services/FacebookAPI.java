package com.example.hongsonpham.firstgreeting.controller.extended_services;

import android.os.Bundle;
import android.util.Log;

import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HongSonPham on 3/14/18.
 */

public class FacebookAPI {
    private CallbackManager callbackManager;
    FirebaseAPI firebaseAPI;

    public FacebookAPI() {
        callbackManager = CallbackManager.Factory.create();
        firebaseAPI = new FirebaseAPI();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    public FirebaseAPI getFirebaseAPI() {
        return firebaseAPI;
    }

    public void setFirebaseAPI(FirebaseAPI firebaseAPI) {
        this.firebaseAPI = firebaseAPI;
    }

    public boolean isLoginAlready() {
        return com.facebook.AccessToken.getCurrentAccessToken() != null;
    }

    public void loadImformation() {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture.role(large),cover");
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        //De
                        String fbId = "";
                        String fbName = "";
                        String fbAvatar = "";
                        String fbCover = "";
                        String fbDOB = "";
                        String email = "";
                        String gender = "";

                        if (response != null) {
                            String userDetail = response.getRawResponse();
                            FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                            try {
                                JSONObject jsonObject = new JSONObject(userDetail);
                                Log.e("object", jsonObject.toString());
                                fbId = jsonObject.getString("id");
                                fbName = jsonObject.getString("name");

                                fbAvatar = "https://graph.facebook.com/" + fbId + "/picture?width=960&height=960";
                                if (jsonObject.has("cover")) {
                                    String getInitialCover = jsonObject.getString("cover");

                                    if (getInitialCover.equals("null")) {
                                        jsonObject = null;
                                    } else {
                                        JSONObject JOCover = jsonObject.optJSONObject("cover");
                                        if (JOCover.has("source")) {
                                            JOCover.getString("source");
                                        } else {
                                            fbCover = null;
                                        }
                                    }
                                } else {
                                    fbCover = null;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        FbUser fbUser = new FbUser(fbId, fbName, fbAvatar, fbCover, fbDOB, email, gender);
                        Log.e("test: ", "Loaded");
                        firebaseAPI.pushFbUser(fbUser);
                        moveToHome();
                    }
                }).executeAsync();
    }

    public void processLogin() {
        Log.e("test: ", "Dang login");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("test: ", "Logined");
                        loadImformation();
                    }

                    @Override
                    public void onCancel() {
                        Log.e("test: ", "Login failed");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("test: ", "Login Error");
                    }
                });
    }

    public void moveToHome() {

    }
}
