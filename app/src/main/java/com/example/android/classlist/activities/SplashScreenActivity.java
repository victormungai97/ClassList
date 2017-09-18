package com.example.android.classlist.activities;

import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.SplashScreenFragment;

public class SplashScreenActivity extends MainFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SplashScreenFragment();
    }
}
