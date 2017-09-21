package com.example.android.classlist.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.android.classlist.activities.MainFragmentActivity;
import com.example.android.classlist.fragments.SuggestionFragment;

public class SuggestionActivity extends MainFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SuggestionFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SuggestionActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
