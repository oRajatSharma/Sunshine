package com.example.rajat.sunshine;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends ActionBarActivity implements
        MainActivityFragment.FBLoginResultListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        MainActivityFragment listFragment = new MainActivityFragment();

        getSupportFragmentManager().beginTransaction().
                add(R.id.fragment_container, listFragment, "FBLoginFragment").commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onFBLoginFailure() {

    }

    @Override
    public void onFBLoginSuccess(String option) {

        Log.i(TAG, "Options received " + option);

        ImageListFragment imageListFragment = (ImageListFragment) getSupportFragmentManager().
                findFragmentByTag("FBImageList");

        if (imageListFragment != null) {
            imageListFragment.fbOptionSelected(option);
        } else {
            ImageListFragment newFragment = new ImageListFragment();
            Bundle args = new Bundle();
            args.putString(newFragment.SELECTED_OPTION, option);
            newFragment.setArguments(args);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }


    }
}
