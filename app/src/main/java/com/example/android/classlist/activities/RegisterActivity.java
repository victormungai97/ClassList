package com.example.android.classlist.activities;

import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.RegisterFragment;

public class RegisterActivity extends MainFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
