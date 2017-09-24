package com.example.android.classlist.activities;

import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.LoginFragment;

public class LoginActivity extends MainFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
