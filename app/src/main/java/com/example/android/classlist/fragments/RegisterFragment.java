package com.example.android.classlist.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.classlist.activities.PictureActivity;
import com.example.android.classlist.others.GridLayoutAdapter;
import com.example.android.classlist.others.Other.Extras;
import com.example.android.classlist.activities.LoginActivity;
import com.example.android.classlist.others.Message;
import com.example.android.classlist.others.Permissions;
import com.example.android.classlist.others.Post;
import com.example.android.classlist.R;
import com.example.android.classlist.activities.RegisterActivity;
import com.example.android.classlist.activities.SuggestionActivity;
import com.example.android.classlist.others.RecyclerViewAdapter;
import com.example.android.classlist.others.URLS;
import com.example.android.classlist.database.SignBaseHelper;
import com.example.android.classlist.database.SignInDbSchema.SignInTable;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.classlist.others.Other.Constants.*;
import static com.example.android.classlist.others.PictureUtilities.compressImage;
import static com.example.android.classlist.others.PictureUtilities.galleryAddPic;
import static com.example.android.classlist.others.PictureUtilities.recogniseFace;
import static com.example.android.classlist.others.PictureUtilities.takePicture;
import static com.example.android.classlist.others.Post.GET;
import static com.example.android.classlist.others.Post.getContentValues;
import static com.example.android.classlist.others.URLS.student_register;

/**
 * Fragment hosted by Register Activity. Allows user to register to the attendance system.
 * Created by User on 7/31/2017.
 */

public class RegisterFragment extends Fragment implements Extras {

    private EditText first_name;
    private EditText last_name;
    private EditText admission_num;
    private Button mSignInButton;
    private ImageView mImageView;
    private static Uri imageForUpload;
    private RecyclerView recyclerViewMain;
    private ArrayList<Bitmap> images = new ArrayList<>();
    private ArrayList<Uri> uris = new ArrayList<>();

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerViewAdapter adapter;
    Spinner department;
    Spinner years;
    ProgressBar progressBar;
    EditText mServerUrl;
    FloatingActionButton fab;
    ImageButton mImageButton;
    Parcelable mListState;

    Bitmap photo;
    static Map<Integer, Bitmap> bitmaps;
    MyTextWatcher firstNameWatcher;
    MyTextWatcher regWatcher;
    MyTextWatcher lastNameWatcher;
    SQLiteDatabase mDatabase;
    String dept;
    String yr;
    File directory;
    static int position;

    public static Uri getImageForUpload() {
        return imageForUpload;
    }

    public static void setImageForUpload(Uri imageForUpload) {
        RegisterFragment.imageForUpload = imageForUpload;
    }

    public static Map<Integer, Bitmap> getBitmaps() {
        return bitmaps;
    }

    public static int getPosition() {
        return position;
    }

    public static RegisterFragment newInstance(Uri uri, int position, ArrayList<Parcelable> images) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_PICTURE, uri);
        args.putInt(ARG_USER_POSITION, position);
        args.putParcelableArrayList(ARG_USER_IMAGES, images);
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check and request any permissions not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Permissions.checkPermission(getActivity(), getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);

        // initialize widgets
        first_name = view.findViewById(R.id.firstName);
        last_name = view.findViewById(R.id.lastName);
        admission_num = view.findViewById(R.id.admissionNum);
        department = view.findViewById(R.id.department);
        department.setOnItemSelectedListener(new SelectItem(DEPARTMENT));
        years = view.findViewById(R.id.year);
        years.setOnItemSelectedListener(new SelectItem(YEAR));
        progressBar = view.findViewById(R.id.simpleProgressBar);
        mSignInButton = view.findViewById(R.id.register_btn);
        // mImageView = view.findViewById(R.id.register_photo);
        fab = view.findViewById(R.id.fab);
        mServerUrl = view.findViewById(R.id.ur_name_register);
        mImageButton = view.findViewById(R.id.register_photo_btn);
        mDatabase = new SignBaseHelper(getActivity()).getWritableDatabase();
        recyclerViewMain = view.findViewById(R.id.fragment_register_recycler_view);
        bitmaps = new HashMap<>();
        initializeImages();
        setAdapter();
        setGridLayoutManager();

        if (getArguments() != null){
            Log.e(TAG,"I'm back");
            position = getArguments().getInt(ARG_USER_POSITION);
            // imageForUpload = getArguments().getParcelable(ARG_USER_PICTURE);
            uris = getArguments().getParcelableArrayList(ARG_USER_IMAGES);
            assert uris != null;
            for (Uri imageForUpload: uris) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                            imageForUpload);
                    try {
                        images.add(photo);
                    } catch (Exception ex) {
                        Log.e("PICTURES " + ERROR, "Something went wrong");
                        ex.printStackTrace();
                    }
                    photo = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // redirect to suggestion activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SuggestionActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // listen to typing of edit text
        regWatcher = new MyTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                department.setAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item,GET(URLS.departments + "?text=" +
                                admission_num.getText().toString(),
                        getActivity().getApplicationContext()).toArray()));
                empty = editable.toString().length() == 0;
                updateSubmitButtonState();
            }
        };

        firstNameWatcher = new MyTextWatcher() {
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

        lastNameWatcher = new MyTextWatcher() {
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

        // set text watchers to respective edit texts
        first_name.addTextChangedListener(firstNameWatcher);
        last_name.addTextChangedListener(lastNameWatcher);
        admission_num.addTextChangedListener(regWatcher);
        mServerUrl.setText(student_register);

        directory = new File(Environment.getExternalStorageDirectory() + File.separator
                + "ClassList" + File.separator + "Pictures");
        // if directory does not exist
        if (!directory.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs(); // create directory and any immediate required directories
        }
        Log.e("Directory:",""+directory.getAbsolutePath());

        // check if button is present before continuing
        if (mImageButton != null) {
            // take picture by clicking button
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageForUpload = takePicture(RegisterFragment.this, TAG, directory.getAbsolutePath());
                    galleryAddPic(getActivity());
                }
            });
        }

        // take picture by clicking image view
        /* mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageForUpload = takePicture(RegisterFragment.this, TAG, directory.getAbsolutePath());
                galleryAddPic(getActivity());
            }
        }); */

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make the progress bar visible
                progressBar.setVisibility(View.VISIBLE);
                // make other elements unusable
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                String name = first_name.getText().toString() + " "
                        + last_name.getText().toString(),
                        reg_no = admission_num.getText().toString();
                ArrayList<String> imagesList = new ArrayList<>();
                for (Bitmap photo: images) {
                    String image = compressImage(photo);
                    imagesList.add(image);
                }
                try {
                    HashMap<String, Object> args = new HashMap<>();
                    args.put(NAME, name);
                    args.put(REG_NO, reg_no);
                    args.put(IMAGES, imagesList);
                    args.put(DEPARTMENT, dept);
                    args.put(YEAR, yr);
                    Message message = new Message(args);
                    JSONObject response = Post.POST(URLS.student_register, message, getActivity());
                    if (response.getInt("status") == 0){
                        ContentValues values = getContentValues(reg_no,name);
                        mDatabase.insert(SignInTable.NAME, null, values);
                        moveToScreen();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Error: " + response.getString("message"),
                                Toast.LENGTH_SHORT ).show();
                    }
                } catch (Exception ex){
                    Log.e(RegisterActivity.class.toString(), "Error connecting to login activity\n"+
                            ex.getMessage());
                }
                // make progress bar invisible
                progressBar.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        return view;
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
                    photo = recogniseFace(imageForUpload, mImageView, getActivity());
                    updateSubmitButtonState();
                } else {
                    Toast.makeText(getActivity(),"Error2 while capturing image",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("BITMAP "+ERROR,ex.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save recycler view state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putParcelableArrayList(PHOTO,images);
        outState.putString(FIRST_NAME,first_name.getText().toString());
        outState.putString(LAST_NAME,last_name.getText().toString());
        outState.putString(REG_NO,admission_num.getText().toString());
        outState.putString(URL,mServerUrl.getText().toString());
        outState.putString(DEPARTMENT, dept);
        outState.putString(YEAR, yr);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // retrieve contents on screen before rotation
        if (savedInstanceState != null) {
            images = savedInstanceState.getParcelableArrayList(PHOTO);
            // mImageView.setImageBitmap(photo);
            first_name.setText(savedInstanceState.getString(FIRST_NAME));
            last_name.setText(savedInstanceState.getString(LAST_NAME));
            admission_num.setText(savedInstanceState.getString(REG_NO));
            mServerUrl.setText(savedInstanceState.getString(URL));
            department.setPrompt(savedInstanceState.getCharSequence(DEPARTMENT));
            years.setPrompt(savedInstanceState.getCharSequence(YEAR));
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    /**
     * Method checks whether information has been entered before submission
     */
    public void updateSubmitButtonState() {
        if ( /* photo != null && */ firstNameWatcher.nonEmpty() && regWatcher.nonEmpty() && lastNameWatcher.nonEmpty() ) {
            mSignInButton.setEnabled(true);
        } else {
            mSignInButton.setEnabled(false);
        }
    }

    @Override
    public void moveToScreen(String ...args) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * This class responds to changes in selected items for the two spinners
     */
    private class SelectItem implements AdapterView.OnItemSelectedListener{

        String choice;

        SelectItem(String choice) {
            this.choice = choice;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            // on selecting a spinner item
            if (choice.equals(YEAR)){
                // if year selected
                yr = adapterView.getItemAtPosition(position).toString();
            } else if (choice.equals(DEPARTMENT)){
                // if department chosen
                dept = adapterView.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    private void setGridLayoutManager() {
        recyclerViewMain.setHasFixedSize(true); // tell RecyclerView that size will remain constant
        int rows = 5;
        // detect orientation and change no. of rows accordingly
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            rows = 2;
        mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), rows);
        recyclerViewMain.setLayoutManager(mLayoutManager);

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = PictureActivity.newIntent(getActivity(), directory.getAbsolutePath(),
                        i/* , getImageForUpload() */, uris);
                startActivity(intent);
                getActivity().finish();
            }
        });
        adapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new GridLayoutAdapter(getActivity(), images);
            recyclerViewMain.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initializeImages() {
        /* images = new ArrayList<>();
        Collections.addAll(images, Other.IMAGES); */
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
