package com.example.rajat.sunshine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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


    public interface FBLoginResultListener {
        public void onFBLoginSuccess();

        public void onFBLoginFailure();
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        mCurrentFragment = this;

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

        final Button loginButton;
        loginButton = (Button) view.findViewById(R.id.login_button);

        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Log.d(TAG, "AccessToken Null");
            loginButton.setText("Login");
        } else {
            Log.d(TAG, "AccessToken not Null. Already logged in" + accessToken.toString());
            loginButton.setText("Logout");
        }

        loginButton.setOnClickListener(loginClickListener);

        // Callback registration
        loginManager.registerCallback(callbackManager, loginResultFacebookCallback);

        return view;
    }

    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<String> fbPermissions = Arrays.asList("user_photos", "user_videos");
            if (((Button) view).getText() == "Login") {
                Log.d(TAG, "Initiating Login");
                loginManager.logInWithReadPermissions(mCurrentFragment, fbPermissions);
            } else {
                Log.d(TAG, "Initiating Logout");
                loginManager.logOut();
            }
        }
    };


    FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            accessToken = AccessToken.getCurrentAccessToken();
            Log.e(TAG, "Facebook login successful " + accessToken);
            TextView successMsg = (TextView) getActivity().findViewById(R.id.resultMsg);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            Log.d(TAG, "Permissions: " + accessToken.getPermissions().toString());
            successMsg.setText("Login Successful" + accessToken.getPermissions().toString());
            successMsg.setVisibility(View.VISIBLE);

            mListener.onFBLoginSuccess();

/*

            ImageListFragment imageListFragment;
            imageListFragment = new ImageListFragment();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, ((Fragment) imageListFragment));
            ft.commit();
*/

        }

        @Override
        public void onCancel() {
            // App code
            Log.e(TAG, "Facebook login cancelled");

            TextView cancelMsg = (TextView) getActivity().findViewById(R.id.resultMsg);
            cancelMsg.setText("Login Cancelled");
            cancelMsg.setVisibility(View.VISIBLE);

            mListener.onFBLoginFailure();
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Log.e(TAG, "Error in Facebook login");
            TextView errorMsg = (TextView) getActivity().findViewById(R.id.resultMsg);
            errorMsg.setText("Login Error" + exception.toString());
            errorMsg.setVisibility(View.VISIBLE);

            mListener.onFBLoginFailure();
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
