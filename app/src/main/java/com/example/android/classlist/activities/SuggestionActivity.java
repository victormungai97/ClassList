package com.example.android.classlist.activities;

import android.support.v4.app.Fragment;

import com.example.android.classlist.activities.MainFragmentActivity;
import com.example.android.classlist.fragments.SuggestionFragment;

public class SuggestionActivity extends MainFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SuggestionFragment();
    }
}
