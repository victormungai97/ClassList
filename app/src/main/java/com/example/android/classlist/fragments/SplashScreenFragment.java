package com.example.android.classlist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.classlist.R;
import com.example.android.classlist.activities.LoginActivity;

/**
 * Fragment for the Splash Screen Activity. Will welcome the user
 * Created by User on 7/31/2017.
 */

public class SplashScreenFragment extends Fragment {
    // time for splash screen to last in milliseconds
    private static final long SPLASH_TIME = 3000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen,container,false);

        new Handler().postDelayed(new Runnable() {

            /*
            * Showing splash screen with given run time.
             */
            @Override
            public void run() {
                // Method will run once splash time is over
                // Start login activity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish(); // close this activity
            }
        }, SPLASH_TIME);

        return view;
    }

    /*
     * A LITTLE SOMETHING
     * God of the Hills and Valleys. I am not alone
     */
}
