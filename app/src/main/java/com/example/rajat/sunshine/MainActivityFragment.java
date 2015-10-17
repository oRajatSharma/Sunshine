package com.example.rajat.sunshine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG="MainActivityFragment";
    private static final String OPEN_WEATHER_MAP_APP_ID = "fdc95bb294937095a6b1a93e502c0da3";
    CallbackManager callbackManager;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        /*accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        LoginButton loginButton;
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        List<String> fbPermissions = Arrays.asList("user_photos", "user_videos");

        loginButton.setReadPermissions(fbPermissions);

        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = AccessToken.getCurrentAccessToken();
                Log.e(TAG,"Facebook login successful " + accessToken);
/*
                View loginBtn = getActivity().findViewById(R.id.login_button);
                loginBtn.setVisibility(View.GONE);
*/
                View successMsg = getActivity().findViewById(R.id.successMsg);
                successMsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                // App code
                Log.e(TAG,"Facebook login cancelled");
                View loginBtn = getActivity().findViewById(R.id.login_button);
                loginBtn.setVisibility(View.GONE);
                View cancelMsg = getActivity().findViewById(R.id.cancelMsg);
                cancelMsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e(TAG,"Error in Facebook login");
                View loginBtn = getActivity().findViewById(R.id.login_button);
                loginBtn.setVisibility(View.GONE);
                View errorMsg = getActivity().findViewById(R.id.errorMsg);
                errorMsg.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        accessTokenTracker.stopTracking();
    }
}
