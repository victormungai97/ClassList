package com.example.android.classlist.activities;

import android.content.Context;
import android.content.Intent;
// import android.net.Uri;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.PictureFragment;

import java.util.ArrayList;

import static com.example.android.classlist.fragments.PictureFragment.getImageForUpload;
import static com.example.android.classlist.fragments.PictureFragment.getImages;
import static com.example.android.classlist.fragments.PictureFragment.getPhoto;
import static com.example.android.classlist.fragments.PictureFragment.getPosition;
import static com.example.android.classlist.fragments.PictureFragment.setImageForUpload;
import static com.example.android.classlist.fragments.PictureFragment.setPhoto;
import static com.example.android.classlist.others.Other.Constants.*;

public class PictureActivity extends MainFragmentActivity {

    /**
     * Method to called in RegisterFragment to create Intent containing extra info as needed.
     * Helps to hide PictureActivity's needed extras
     * @param packageContext current context of application
     * @param dir Directory to save images
     * @param position Position of view in the adapter
     * /* @param uri URI of picture already taken
     * @return intent to be created
     */
    public static Intent newIntent(Context packageContext, String dir, int position/* , Uri uri */, ArrayList images){
        Intent intent = new Intent(packageContext, PictureActivity.class);
        intent.putExtra(EXTRA_USER_DIR, dir);
        intent.putExtra(EXTRA_USER_POSITION, position);
        intent.putExtra(EXTRA_USER_PICTURE, images);
        // intent.putExtra(EXTRA_USER_PICTURE, uri);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // retrieve saved key-value intent pair and pass to fragment
        String dir = getIntent().getStringExtra(EXTRA_USER_DIR);
        int position = getIntent().getIntExtra(EXTRA_USER_POSITION, 0);
        ArrayList<Uri> images = getIntent().getParcelableArrayListExtra(EXTRA_USER_PICTURE);
        // Uri uri = getIntent().getParcelableExtra(EXTRA_USER_PICTURE);
        return PictureFragment.newInstance(dir, position/* , uri */, images);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getPhoto() != null)
        {
            getPhoto().recycle();
            setPhoto(null);
        }
        if (getImageForUpload() != null){
            setImageForUpload(null);
        }
        finish();
    }
}
