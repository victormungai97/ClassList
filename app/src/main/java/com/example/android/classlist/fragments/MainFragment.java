package com.example.android.classlist.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.classlist.activities.MainActivity;
import com.example.android.classlist.R;
import com.example.android.classlist.others.LocatingClass;
import com.example.android.classlist.others.Message;
import com.example.android.classlist.others.Permissions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.classlist.others.Other.Constants.*;
import static com.example.android.classlist.others.PictureUtilities.compressImage;
import static com.example.android.classlist.others.PictureUtilities.galleryAddPic;
import static com.example.android.classlist.others.PictureUtilities.getImageUri;
import static com.example.android.classlist.others.PictureUtilities.getRealPathFromURI;
import static com.example.android.classlist.others.PictureUtilities.recogniseFace;
import static com.example.android.classlist.others.PictureUtilities.takePicture;
// import static com.example.android.classlist.others.Post.processResults;

/**
 * Fragment hosted by Main Activity. Allows user to sign into respective class
 * Created by User on 8/2/2017.
 */

public class MainFragment extends Fragment {

    private ImageView mImageView;
    private Uri imageForUpload;
    private Button mButton;
    private EditText full_name;
    private EditText adm_num;
    private EditText mServerUrl;

    private LocationManager locationManager;

    boolean mIsConnected = false;
    String name;
    String reg_no;
    int status = 0;
    String message;

    LocatingClass mLocatingClass; // instance of locating class
    GoogleApiClient mClient; // Google Play Services instance
    Bitmap photo = null;
    MyTextWatcher urlTextWatcher;
    MyTextWatcher nameWatcher;
    MyTextWatcher regWatcher;
    String mast;
    String directory;
    Context mContext;
    String mCurrentPath;

    /**
     * Accepts values sent to hosting activity, packs it in a bundle and returns a fragment
     * @param full_name Full name of registered user
     * @param reg_num Registration number of user
     * @param dir Directory to save picture
     * @return instance of the fragment
     */
    public static MainFragment newInstance(String full_name, String reg_num, String dir) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_FULL_NAME, full_name);
        args.putString(ARG_USER_REG_NUM, reg_num);
        args.putString(ARG_USER_DIR, dir);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        This is called before initializing the camera because the camera needs permissions(the cause of the crash)
        Also checks for other dangerous permissions like location and phone network
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            Permissions.checkPermission(getActivity(), getActivity());
        }

        mContext = getContext();
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mIsConnected = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        try {
            mast = LocatingClass.getCellInfo(mContext).get("name").toString();
        } catch (JSONException ex){
            Log.e(MainActivity.class.toString(), ex.getMessage());
        }

        name = getArguments().getString(ARG_USER_FULL_NAME);
        reg_no = getArguments().getString(ARG_USER_REG_NUM);
        directory = getArguments().getString(ARG_USER_DIR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        mImageView = view.findViewById(R.id.image_view);
        mButton = view.findViewById(R.id.submit_btn);
        full_name = view.findViewById(R.id.full_name);
        adm_num = view.findViewById(R.id.admission_num);
        mServerUrl = view.findViewById(R.id.ur_name_main);

        urlTextWatcher = new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                empty = editable.toString().length() == 0;
                updateSubmitButtonState();
            }
        };

        nameWatcher = new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                empty = editable.toString().length() == 0;
                updateSubmitButtonState();
            }
        };

        regWatcher = new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                empty = editable.toString().length() == 0;
                updateSubmitButtonState();
            }
        };

        mServerUrl.addTextChangedListener(urlTextWatcher);
        full_name.addTextChangedListener(nameWatcher);
        adm_num.addTextChangedListener(regWatcher);

        mServerUrl.setText(URL_TO_SEND_DATA);
        full_name.setText(name);
        adm_num.setText(reg_no);

        //prevent edition
        if (name != null){
            full_name.setEnabled(false);
        }

        if (reg_no != null){
            adm_num.setEnabled(false);
        }

        turnGPSOn();
        imageForUpload = takePicture(MainFragment.this, TAG, directory);
        galleryAddPic(getActivity());

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageForUpload = takePicture(MainFragment.this, TAG, directory);
                galleryAddPic(getActivity());
            }
        });

        // client is instance of GoogleApiClient class and enables use of Play Services
        mClient = new GoogleApiClient.Builder(mContext)
                // add specific Play API to use ie Location API
                .addApi(LocationServices.API)
                // pass connection state information
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {}

                    @Override
                    public void onConnectionSuspended(int i) {
                        Toast.makeText(getActivity(),"Suspended",Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .build();

        mLocatingClass = new LocatingClass(getActivity().getApplicationContext(),mClient);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsConnected) {
                    turnGPSOn();
                } else {
                    try {
                        ArrayList<Double> location = mLocatingClass.findLocation();
                        Log.i(TAG, location.toString());
                        String phone = mLocatingClass.getPhone();
                        String time = mLocatingClass.getTime();
                        String latitude = String.valueOf(mLocatingClass.getLatitude());
                        String longitude = String.valueOf(mLocatingClass.getLongitude());
                        String altitude = String.valueOf(mLocatingClass.getAltitude());
                        JSONObject jsonObject = (JSONObject) LocatingClass.getCellInfo(getActivity()).get("primary");
                        String lac = String.valueOf(jsonObject.getInt("LAC")), ci = String.valueOf(jsonObject.getInt("CID"));

                        //JSONObject param = new JSONObject();
                        String name = full_name.getText().toString();
                        String regno = adm_num.getText().toString();

                        /* compress image */
                        //bm is the bitmap object
                        String image = compressImage(photo);
                        String url = mServerUrl.getText().toString();

                        new HttpsRequest().execute(name, regno, time, image, latitude, longitude,
                                lac, ci, url, phone, altitude);
                        if (status == 0) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException ex) {
                        Log.e(TAG, "Error reading cell info " + ex.getMessage());
                    }
                }
            }
        });

        return view;
    }

    /**
     * Method checks whether information has been entered before submission
     */
    public void updateSubmitButtonState() {
        if (photo != null && urlTextWatcher.nonEmpty() && nameWatcher.nonEmpty() && regWatcher.nonEmpty()) {
            mButton.setEnabled(true);
        } else {
            mButton.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        b.putParcelable("image", photo);
        b.putString("url", mServerUrl.getText().toString());
        b.putString("name", full_name.getText().toString());
        b.putString("reg", adm_num.getText().toString());
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        if (b != null) {
            photo = b.getParcelable("image");
            mImageView.setImageBitmap(photo);
            mServerUrl.setText(b.getString("url"));
            full_name.setText(b.getString("name"));
            adm_num.setText(b.getString("reg"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
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
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
                if (imageForUpload != null) {
                    photo = recogniseFace(imageForUpload, mImageView, getActivity());
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    mCurrentPath = getRealPathFromURI(tempUri,getActivity());
                    Log.e(TAG,mCurrentPath);
                    updateSubmitButtonState();
                } else {
                    Toast.makeText(getActivity(),"Error2 while capturing image",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("BITMAP error",ex.getMessage());
        }
    }


    /**
     * Checks whether field is empty
     */
    abstract class MyTextWatcher implements TextWatcher {
        boolean empty = true;

        boolean nonEmpty() {
            return !empty;
        }
    }

    /**
     * Method to check whether GPS is on
     */
    private void turnGPSOn(){
        mIsConnected = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!mIsConnected){
            new Permissions().showSettingsAlert(mContext);
        }
    }

    private class HttpsRequest extends AsyncTask<String, Void, Void> {

        ProgressDialog pDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait");
            pDialog.setIndeterminate(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... jsonObjects) {
            String name = jsonObjects[0];
            String reg_no = jsonObjects[1];
            String time = jsonObjects[2];
            String pic = jsonObjects[3];
            String latitude = jsonObjects[4];
            String longitude = jsonObjects[5];
            String lac = jsonObjects[6];
            String ci = jsonObjects[7];
            String url = jsonObjects[8];
            String phone = jsonObjects[9];
            String altitude = jsonObjects[10];

            HashMap<String, String> args = new HashMap<>();
            args.put(REG_NO, reg_no);
            args.put(NAME, name);
            args.put(TIME, time);
            args.put(PIC, pic);
            args.put(PHONE, phone);
            args.put(LATITUDE, latitude);
            args.put(LONGITUDE, longitude);
            args.put(ALTITUDE, altitude);
            args.put(LAC, lac);
            args.put(CI, ci);

            Message msg = new Message(args);
            String TAG = "SIGNING IN SUCCESS ", ERROR = "Signing in Error: ";
            JSONObject jsonObject = null;
            // jsonObject = processResults(TAG, POST(url,msg), ERROR);
            try {
                status = jsonObject.getInt("STATUS");
                message = jsonObject.getString("MESSAGE");
            } catch (JSONException ex){
                Log.e("JSON error", "Error sending data "+ex.getMessage());
            } catch (NullPointerException ex){
                Log.e("Null Pointer", "Something went wrong "+ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()){
                pDialog.cancel();
            }
        }
    }
}
