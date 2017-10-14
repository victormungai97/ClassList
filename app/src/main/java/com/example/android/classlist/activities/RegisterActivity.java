package com.example.android.classlist.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.RegisterFragment;

import java.util.ArrayList;

import static com.example.android.classlist.fragments.RegisterFragment.getImageForUpload;
import static com.example.android.classlist.fragments.RegisterFragment.setImageForUpload;
import static com.example.android.classlist.others.Other.Constants.*;

public class RegisterActivity extends MainFragmentActivity {

    @Override
    protected Fragment createFragment() {
        // retrieve saved key-value intent pair and pass to fragment
        Uri uri = getIntent().getParcelableExtra(EXTRA_USER_PICTURE);
        int position = getIntent().getIntExtra(EXTRA_USER_POSITION, 0);
        ArrayList images = getIntent().getParcelableArrayListExtra(EXTRA_USER_IMAGES);
        if (uri != null)
            return RegisterFragment.newInstance(uri, position, images);
        else {
            return new RegisterFragment();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static Intent newIntent(Context packageContext, Uri uri, int position, ArrayList images){
        Intent intent = new Intent(packageContext, RegisterActivity.class);
        intent.putExtra(EXTRA_USER_PICTURE, uri);
        intent.putExtra(EXTRA_USER_POSITION, position);
        intent.putExtra(EXTRA_USER_IMAGES, images);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getImageForUpload() == null){
            setImageForUpload(null);
        }
    }
}
