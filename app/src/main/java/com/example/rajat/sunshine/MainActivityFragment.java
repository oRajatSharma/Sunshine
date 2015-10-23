package com.example.rajat.sunshine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG="MainActivityFragment";
    CallbackManager callbackManager;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    LoginManager loginManager;
    FBLoginResultListener mListener;
    Fragment mCurrentFragment;
    ListView mFBOptions;
    TextView mSuccessMsg;
    Button loginButton;

    public interface FBLoginResultListener {
        void onFBLoginSuccess(String option);

        void onFBLoginFailure();
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        mCurrentFragment = this;

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                Log.i(TAG, "Access Token Changed");
                accessToken = AccessToken.getCurrentAccessToken();
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        loginButton = (Button) view.findViewById(R.id.login_button);
        mFBOptions = (ListView) view.findViewById(R.id.FBOptions);
        mSuccessMsg = (TextView) view.findViewById(R.id.resultMsg);

        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            Log.i(TAG, "AccessToken Null");
            loginButton.setText("Login");
            mSuccessMsg.setVisibility(View.GONE);
            mFBOptions.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "AccessToken not Null. Already logged in" + accessToken.toString());
            Log.i(TAG, "Access Token expiry " + accessToken.getExpires());
            loginButton.setText("Logout");
            mSuccessMsg.setVisibility(View.VISIBLE);
            mFBOptions.setVisibility(View.VISIBLE);
        }

        loginButton.setOnClickListener(loginClickListener);

        // Callback registration
        loginManager.registerCallback(callbackManager, loginResultFacebookCallback);

        mFBOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> mFBOptions, View view, int position, long rowId) {
                Log.i(TAG, "Item clicked position = " + position + " rowId = " + rowId);
                TextView textView = (TextView) view;
                String clickedText = textView.getText().toString();

                Log.i(TAG, "Clicked Item : " + clickedText);
                mListener.onFBLoginSuccess(clickedText);
            }
        });

        return view;
    }

    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<String> fbPermissions = Arrays.asList("user_photos", "user_videos");
            if (((Button) view).getText() == "Login") {
                Log.i(TAG, "Initiating Login");
                loginManager.logInWithReadPermissions(mCurrentFragment, fbPermissions);
            } else {
                Log.i(TAG, "Initiating Logout");
                loginManager.logOut();
                mFBOptions.setVisibility(View.GONE);
                mSuccessMsg.setVisibility(View.GONE);
                ((Button) view).setText("Login");
            }
        }
    };


    FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            accessToken = AccessToken.getCurrentAccessToken();
            Log.e(TAG, "Facebook login successful " + accessToken);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            Log.i(TAG, "Permissions: " + accessToken.getPermissions().toString());
            mSuccessMsg.setText("Login Successful" + accessToken.getPermissions().toString());
            Log.i(TAG, "Access Token expiry " + accessToken.getExpires());

            loginButton.setText("Logout");
            mSuccessMsg.setVisibility(View.VISIBLE);
            mFBOptions.setVisibility(View.VISIBLE);


        }

        @Override
        public void onCancel() {
            // App code
            Log.e(TAG, "Facebook login cancelled");

            mSuccessMsg.setText("Login Cancelled");
            mSuccessMsg.setVisibility(View.VISIBLE);

//            mListener.onFBLoginFailure();
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Log.e(TAG, "Error in Facebook login");
            mSuccessMsg.setText("Login Error" + exception.toString());
            mSuccessMsg.setVisibility(View.VISIBLE);

//            mListener.onFBLoginFailure();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (FBLoginResultListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FBLoginResultListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        accessTokenTracker.stopTracking();
    }
}
