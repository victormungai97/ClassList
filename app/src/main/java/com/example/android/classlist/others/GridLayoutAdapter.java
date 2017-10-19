package com.example.android.classlist.others;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
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

import static com.example.android.classlist.others.Other.Constants.*;
import static com.example.android.classlist.others.PictureUtilities.getRealPathFromURI;

/**
 * Created by victor on 10/11/17.
 * Handles the arrangement of views in a grid layout format
 * Courtesy of the https://appguruz.com
 */

public class GridLayoutAdapter extends RecyclerViewAdapter {

    private Activity activity;
    private ArrayList<Uri> images;
    private int screenWidth;

    public GridLayoutAdapter(Activity activity, ArrayList<Uri> images) {
        this.activity = activity;
        this.images = images;
        // get size of screen display using WindowManager and Point object class
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
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
        try {
            final Holder myHolder = (Holder) holder;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(getRealPathFromURI(images.get(position), activity));
            options.inJustDecodeBounds = false;
            Picasso.with(activity)
                    .load(images.get(position))
                    .placeholder(R.mipmap.ic_picture)
                    .error(R.mipmap.ic_picture)
                    .resize(screenWidth / 2, 300)
                    .centerCrop()
                    .into(myHolder.images);
        } catch (Exception ex){
            Log.e("PICTURES "+ERROR, "Something went wrong");
            ex.printStackTrace();
        }
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
