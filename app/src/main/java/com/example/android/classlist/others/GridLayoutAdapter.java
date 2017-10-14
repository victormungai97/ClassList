package com.example.android.classlist.others;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.android.classlist.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import static com.example.android.classlist.fragments.RegisterFragment.getBitmaps;
import static com.example.android.classlist.fragments.RegisterFragment.getPosition;
import static com.example.android.classlist.others.Other.Constants.*;

/**
 * Created by victor on 10/11/17.
 * Handles the arrangement of views in a grid layout format
 * Courtesy of the appguruz.com
 */

public class GridLayoutAdapter extends RecyclerViewAdapter {

    private Activity activity;
    private ArrayList<Bitmap> images;
    private int screenWidth;

    public GridLayoutAdapter(Activity activity, ArrayList<Bitmap> images) {
        this.activity = activity;
        this.images = images;
        // get size of screen display using WindowManager and Point object class
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    /*
     * Used to create new ViewHolder, along with its returned View to display.
     * Called until a sufficient number of ViewHolders have been created,
     * after which the old ViewHolders are recycled, saving space and time
     */
    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.grid_pictures, parent, false);
        return new Holder(view);
    }

    /*
     * Used to display the data at the specified position.
     * It updates the content of the itemView to reflect the item at the given position.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Holder myHolder = (Holder) holder;
        try {
            myHolder.images.setImageBitmap(images.get(position));
        } catch (Exception ex){
            Log.e("PICTURES "+ERROR, "Something went wrong");
            ex.printStackTrace();
        }
        /* if (getBitmaps().get(position) != null && getPosition() == position){
            myHolder.images.setImageBitmap(getBitmaps().get(position));
        } else {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(images.get(position), opts);
            opts.inJustDecodeBounds = false;
            Picasso.with(activity)
                    .load(images.get(position))
                    .error(R.mipmap.ic_picture)
                    .placeholder(R.mipmap.ic_picture)
                    .resize(screenWidth / 2, 300)
                    .centerCrop()
                    .into((myHolder.images));
        } */
    }

    /*
     * Returns the number of objects in list for preparation of space
     */
    @Override
    public int getItemCount() {
        Log.e("IMAGE SIZE", ""+images.size());
        return 10;
    }

    private class Holder extends RecycleViewHolder {
        ImageView images;

        Holder(View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.list_item_image_view);
        }
    }
}
