package com.example.android.classlist.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.classlist.R;
import com.example.android.classlist.activities.RegisterActivity;

import java.util.ArrayList;

import static com.example.android.classlist.others.Other.Constants.*;
import static com.example.android.classlist.others.PictureUtilities.galleryAddPic;
import static com.example.android.classlist.others.PictureUtilities.getImageUri;
import static com.example.android.classlist.others.PictureUtilities.recogniseFace;
import static com.example.android.classlist.others.PictureUtilities.takePicture;

/**
 * Created by victor on 10/7/17.
 * This fragment will be used to scale images to full screen
 */

public class PictureFragment extends Fragment implements View.OnClickListener {

    ImageView imageView;
    static Uri imageForUpload;
    static Bitmap photo;
    static int position;
    String dir;
    static ArrayList<Uri> images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* explicitly tell the FragmentManager that your fragment should receive a call to
         * onCreateOptionsMenu(...) */
        setHasOptionsMenu(true);
    }

    public static Bitmap getPhoto() {
        return photo;
    }

    public static Uri getImageForUpload() {
        return imageForUpload;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPhoto(Bitmap photo) {
        PictureFragment.photo = photo;
    }

    public static void setImageForUpload(Uri imageForUpload) {
        PictureFragment.imageForUpload = imageForUpload;
    }

    public static ArrayList<Uri> getImages() {
        return images;
    }

    /**
     * Accepts values sent to hosting activity, packs it in a bundle and returns a fragment
     * @param dir Directory to save picture
     * @param position Position of passed image view and corresponding image uri in adapter
     * @return instance of the fragment
     */
    public static PictureFragment newInstance(String dir, ArrayList<Uri> images, int position) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_DIR, dir);
        args.putInt(ARG_USER_POSITION, position);
        args.putParcelableArrayList(ARG_USER_PICTURE, images);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture,container,false);

        // initialize widget
        imageView = view.findViewById(R.id.preview_image);
        imageView.setOnClickListener(this);
        // get passed values for position and directory
        dir = getArguments().getString(ARG_USER_DIR);
        images = getArguments().getParcelableArrayList(ARG_USER_PICTURE);
        position = getArguments().getInt(ARG_USER_POSITION);
        if (photo == null){
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_picture));
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // populate Menu instance with items defined in file
        inflater.inflate(R.menu.fragment_picture_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // if user selects picture menu item
            case R.id.menu_item_new_picture:
                onClick(imageView);
                return true;
            default:
                Toast.makeText(getContext(), "Hello, World!", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("photo",photo);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // retrieve contents on screen before rotation
        if (savedInstanceState != null) {
            photo = savedInstanceState.getParcelable("photo");
            if (photo == null){
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_menu_camera));
            } else
                imageView.setImageBitmap(photo);
        }
    }

    /**
     * Method retrieves image sent by return intent as small Bitmap in extras with data as key
     * and displays to ImageView
     * @param requestCode request that was received from take picture
     * @param resultCode result of operation
     * @param data return intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        try {
            if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
                if (imageForUpload != null) {
                    photo = recogniseFace(imageForUpload, imageView, getActivity());
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    if (photo!= null) {
                        imageForUpload = getImageUri(getContext(), photo);
                        // ADD OR REPLACE RETRIEVED URI IN LIST
                        if (images.size() == 10) {
                            images.remove(getPosition());
                            images.add(getPosition(), imageForUpload);
                        }
                        else images.add(imageForUpload);
                        Intent intent = RegisterActivity.newIntent(getActivity(), getImages());
                        startActivity(intent);
                    }
                    else imageForUpload = null;
                } else {
                    Toast.makeText(getActivity(),"Error2 while capturing image",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("BITMAP "+ERROR, ex.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "Smile For The Camera!", Toast.LENGTH_SHORT).show();
        imageForUpload = takePicture(PictureFragment.this, TAG, dir);
        galleryAddPic(getActivity());
    }
}
