package com.example.android.classlist.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Base64;
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

import com.example.android.classlist.others.Other.Extras;
import com.example.android.classlist.activities.LoginActivity;
import com.example.android.classlist.others.Message;
import com.example.android.classlist.others.Permissions;
import com.example.android.classlist.others.Post;
import com.example.android.classlist.R;
import com.example.android.classlist.activities.RegisterActivity;
import com.example.android.classlist.activities.SuggestionActivity;
import com.example.android.classlist.others.URLS;
import com.example.android.classlist.database.SignBaseHelper;
import com.example.android.classlist.database.SignInDbSchema.SignInTable;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.classlist.others.Other.Constants.*;
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
    private Uri imageForUpload;

    Spinner department;
    Spinner years;
    ProgressBar progressBar;
    EditText mServerUrl;
    FloatingActionButton fab;
    ImageButton mImageButton;

    Bitmap photo;
    MyTextWatcher firstNameWatcher;
    MyTextWatcher regWatcher;
    MyTextWatcher lastNameWatcher;
    SQLiteDatabase mDatabase;
    String dept;
    String yr;

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
        department.setOnItemSelectedListener(new SelectItem("department"));
        years = view.findViewById(R.id.year);
        years.setOnItemSelectedListener(new SelectItem("year"));
        progressBar = view.findViewById(R.id.simpleProgressBar);
        mSignInButton = view.findViewById(R.id.register_btn);
        mImageView = view.findViewById(R.id.register_photo);
        fab = view.findViewById(R.id.fab);
        mServerUrl = view.findViewById(R.id.ur_name_register);
        mImageButton = view.findViewById(R.id.register_photo_btn);
        mDatabase = new SignBaseHelper(getActivity()).getWritableDatabase();

        // redirect to suggestion activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SuggestionActivity.class);
                startActivity(intent);
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

        // check if button is present before continuing
        if (mImageButton != null) {
            // take picture by clicking button
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageForUpload = takePicture(RegisterFragment.this, TAG);
                    galleryAddPic(getActivity());
                }
            });
        }

        // take picture by clicking image view
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageForUpload = takePicture(RegisterFragment.this, TAG);
                galleryAddPic(getActivity());
            }
        });

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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                // convert byte array to byte string
                String image = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                ArrayList <String> imagesList = new ArrayList<>();
                imagesList.add(image);
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
            Log.e("BITMAP error",ex.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("photo",photo);
        outState.putString("FIRST NAME",first_name.getText().toString());
        outState.putString("LAST NAME",last_name.getText().toString());
        outState.putString("REG_NO",admission_num.getText().toString());
        outState.putString("URL",mServerUrl.getText().toString());
        outState.putString("DEPARTMENT", dept);
        outState.putString("YEAR", yr);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // retrieve contents on screen before rotation
        if (savedInstanceState != null) {
            photo = savedInstanceState.getParcelable("photo");
            mImageView.setImageBitmap(photo);
            first_name.setText(savedInstanceState.getString("FIRST NAME"));
            last_name.setText(savedInstanceState.getString("LAST NAME"));
            admission_num.setText(savedInstanceState.getString("REG_NO"));
            mServerUrl.setText(savedInstanceState.getString("URL"));
            department.setPrompt(savedInstanceState.getCharSequence("DEPARTMENT"));
            years.setPrompt(savedInstanceState.getCharSequence("YEAR"));
        }
    }

    /**
     * Method checks whether information has been entered before submission
     */
    public void updateSubmitButtonState() {
        if ( photo != null && firstNameWatcher.nonEmpty() && regWatcher.nonEmpty() && lastNameWatcher.nonEmpty() ) {
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
            if (choice.equals("year")){
                // if year selected
                yr = adapterView.getItemAtPosition(position).toString();
            } else if (choice.equals("department")){
                // if department chosen
                dept = adapterView.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }
}
