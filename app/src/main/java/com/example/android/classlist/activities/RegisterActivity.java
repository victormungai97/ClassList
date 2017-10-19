package com.example.android.classlist.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.RegisterFragment;

import java.util.ArrayList;

import static com.example.android.classlist.others.Other.Constants.*;

public class RegisterActivity extends MainFragmentActivity {

    public static Intent newIntent(Context packageContext, ArrayList images){
        Intent intent = new Intent(packageContext, RegisterActivity.class);
        intent.putExtra(EXTRA_USER_IMAGES, images);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // retrieve saved key-value intent pair and pass to fragment
        ArrayList<Uri> images = getIntent().getParcelableArrayListExtra(EXTRA_USER_IMAGES);
        if (images != null)
            return RegisterFragment.newInstance(images);
        else {
            return new RegisterFragment();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
