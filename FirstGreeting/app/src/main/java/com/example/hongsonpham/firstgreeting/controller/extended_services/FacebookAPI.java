package com.example.hongsonpham.firstgreeting.controller.extended_services;

import android.os.Bundle;
import android.util.Log;

import com.example.hongsonpham.firstgreeting.model.entity.user.FbUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
//        Bundle params = new Bundle();
//        params.putString("fields", "id,name,picture.width(960),cover)");
//        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    @Override
//                    public void onCompleted(GraphResponse response) {
//                        //Declare variables
//                        String fbId = "";
//                        String fbName = "";
//                        String fbAvatar = "";
//                        String fbCover = "";
//                        String fbDOB = "";
//                        String email = "";
//                        String gender = "";
//
//                        if (response != null) {
//                            String userDetail = response.getRawResponse();
//                            FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
//                            try {
//                                JSONObject jsonObject = new JSONObject(userDetail);
//                                Log.e("object", jsonObject.toString());
//
//                                if (jsonObject.has("id")) {
//                                    fbId = jsonObject.getString("id");
//                                }
//
//                                if (jsonObject.has("name")) {
//                                    fbName = jsonObject.getString("name");
//                                }
//
//                                if (jsonObject.has("picture")) {
//                                    fbAvatar = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
//                                }
//
//                                if (jsonObject.has("cover")) {
//                                    fbCover = jsonObject.getJSONObject("cover").getString("source");
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        FbUser fbUser = new FbUser(fbId, fbName, fbAvatar, fbCover, fbDOB, email, gender);
//                        Log.e("test: ", "Loaded");
//                        firebaseAPI.pushFbUser(fbUser);
//                        moveToHome();
//                    }
//                }).executeAsync();

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        //Declare variables
                        String fbId = "";
                        String fbName = "";
                        String fbAvatar = "";
                        String fbCover = "";
                        String fbDOB = "";
                        String email = "";
                        String gender = "";

                        if (object != null) {
                            try {
                                Log.e("object", object.toString());

                                if (object.has("id")) {
                                    fbId = object.getString("id");
                                }

                                if (object.has("name")) {
                                    fbName = object.getString("name");
                                }

                                if (object.has("picture")) {
                                    fbAvatar = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                }

                                if (object.has("cover")) {
                                    fbCover = object.getJSONObject("cover").getString("source");
                                }

                                if (object.has("birthday")) {
                                    fbDOB = object.getString("birthday");
                                }

                                if (object.has("email")) {
                                    email = object.getString("email");
                                }

                                if (object.has("gender")) {
                                    gender = object.getString("gender");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("test: ", "jsonObject is Null");
                        }
                        FbUser fbUser = new FbUser(fbId, fbName, fbAvatar, fbCover, fbDOB, email, gender);
                        Log.e("test: ", "Loaded");
                        firebaseAPI.pushFbUser(fbUser);
                        moveToHome();
                    }
                });
        Bundle parammeters = new Bundle();
        parammeters.putString("fields", "id,name,picture.width(960).heigh(960),cover,birthday,email,gender");
        request.setParameters(parammeters);
        request.executeAsync();
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
