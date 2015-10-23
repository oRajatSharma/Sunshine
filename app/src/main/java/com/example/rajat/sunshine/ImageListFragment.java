package com.example.rajat.sunshine;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

public class ImageListFragment extends Fragment {

    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    public static final String SELECTED_OPTION = "SELECTED_OPTION";
    private static final String TAG = "ImageListFragment";
    String mSelectedOption;

    public ImageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        accessToken = AccessToken.getCurrentAccessToken();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fbOptionsList = inflater.inflate(R.layout.image_list_fragment, container, false);

        Bundle args = getArguments();
        mSelectedOption = args.getString(SELECTED_OPTION);
        fbOptionSelected(mSelectedOption);

        return fbOptionsList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void fbOptionSelected(String option) {

        Log.i(TAG, "Access Token expiry " + accessToken.getExpires());
        if ((accessToken == null) || (accessToken.isExpired())) {
            Log.i(TAG, "Access Token expired, relogin required");
        }

        Log.i(TAG, "Making Graph Request");
//        GraphRequest request = GraphRequest.newMeRequest(
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "me/photos",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(
                            GraphResponse response) {
                        // Application code
                        Log.i(TAG, "Graph Response " + response.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        Log.i(TAG, "Graph request " + request.toString());
        request.executeAsync();
    }

}
