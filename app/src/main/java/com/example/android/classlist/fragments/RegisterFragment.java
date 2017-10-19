package com.example.android.classlist.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Button;
import android.widget.EditText;
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
import com.example.android.classlist.others.URLS;
import com.example.android.classlist.database.SignBaseHelper;
import com.example.android.classlist.database.SignInDbSchema.SignInTable;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

import static com.example.android.classlist.others.FileUtilities.createSystemDirs;
import static com.example.android.classlist.others.Other.Constants.*;
import static com.example.android.classlist.others.PictureUtilities.getFileName;
import static com.example.android.classlist.others.PictureUtilities.getRealPathFromURI;
import static com.example.android.classlist.others.Post.GET;
import static com.example.android.classlist.others.Post.getContentValues;
import static com.example.android.classlist.others.FileUtilities.zip;

/**
 * Fragment hosted by Register Activity. Allows user to register to the attendance system.
 * Created by User on 7/31/2017.
 */

public class RegisterFragment extends Fragment implements Extras {

    private EditText first_name;
    private EditText last_name;
    private EditText email_address;
    private EditText admission_num;
    private Button mSignInButton;
    private RecyclerView recyclerViewMain;
    private ArrayList<Uri> uris = new ArrayList<>();

    RecyclerView.LayoutManager mLayoutManager;
    GridLayoutAdapter adapter;
    Spinner department;
    Spinner years;
    ProgressBar progressBar;
    FloatingActionButton fab;
    Parcelable mListState;

    MyTextWatcher firstNameWatcher;
    MyTextWatcher regWatcher;
    MyTextWatcher lastNameWatcher;
    MyTextWatcher emailAddressWatcher;
    SQLiteDatabase mDatabase;
    String dept;
    String yr;
    File directory;
    File documents;
    // used to set hints to spinners
    SelectItem selectItemDept;
    SelectItem selectItemYear;

    public static RegisterFragment newInstance(ArrayList<Uri> images) {
        Bundle args = new Bundle();
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
        email_address = view.findViewById(R.id.emailAddress);
        admission_num = view.findViewById(R.id.admissionNum);
        department = view.findViewById(R.id.department);
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.Departments_of_study));
        selectItemDept = new SelectItem(department, R.string.dept_prompt, list, YEAR);
        selectItemDept.getHintSpinner().init();
        list = Arrays.asList(getResources().getStringArray(R.array.Years_of_study));
        years = view.findViewById(R.id.year);
        selectItemYear = new SelectItem(years, R.string.year_prompt, list, YEAR);
        selectItemYear.getHintSpinner().init();
        progressBar = view.findViewById(R.id.simpleProgressBar);
        mSignInButton = view.findViewById(R.id.register_btn);
        fab = view.findViewById(R.id.fab);
        mDatabase = new SignBaseHelper(getActivity()).getWritableDatabase();
        recyclerViewMain = view.findViewById(R.id.fragment_register_recycler_view);
        initializeImages();
        setAdapter();
        setGridLayoutManager();

        if (getArguments() != null){
            uris = getArguments().getParcelableArrayList(ARG_USER_IMAGES);
            assert uris != null;
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
                selectItemDept = new SelectItem(department, R.string.dept_prompt,
                        setList(GET(URLS.departments + "?text=" +
                                        admission_num.getText().toString(),
                                getActivity().getApplicationContext())), DEPARTMENT);
                selectItemDept.getHintSpinner().init();
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

        emailAddressWatcher = new MyTextWatcher() {
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
        email_address.addTextChangedListener(emailAddressWatcher);
        admission_num.addTextChangedListener(regWatcher);

        // directories for use
        List<File> file = createSystemDirs(getActivity());
        directory = file.get(0); // pictures
        documents = file.get(1); // documents
        Log.e("Directory",""+directory.getAbsolutePath());
        Log.e("Directory",""+documents.getAbsolutePath());

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make the progress bar visible
                progressBar.setVisibility(View.VISIBLE);
                // make other elements unusable
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                File zip_file = new File(documents, IMAGES + "_" + getFileName() + ".zip");
                String name = first_name.getText().toString() + " "
                        + last_name.getText().toString(),
                        email = email_address.getText().toString(),
                        reg_no = admission_num.getText().toString();

                // GET THE IMAGE FILE PATHS FROM THE URI LIST AND ADD THEM TO ARRAY
                String [] image_files = new String[10];
                for (int counter = 0; counter < uris.size(); counter ++){
                    image_files[counter] = getRealPathFromURI(uris.get(counter), getActivity());
                }
                // call zipping function
                zip(image_files, zip_file.getAbsolutePath());

                // create message, post it to server and react based on received response or error
                try {
                    HashMap<String, Object> args = new HashMap<>();
                    args.put(NAME, name);
                    args.put(REG_NO, reg_no);
                    args.put(EMAIL_ADDRESS, email);
                    args.put(FILE, zip_file);
                    args.put(DEPARTMENT, dept);
                    args.put(YEAR, yr);
                    Message message = new Message(args);
                    JSONObject response = Post.POST(URLS.student_register, message);
                    if (response.getInt("status") == 0){
                        ContentValues values = getContentValues(reg_no,name);
                        mDatabase.insert(SignInTable.NAME, null, values);
                        moveToScreen();
                    } else {
                        // delete created zip in case of failure to save space
                        try {
                            //noinspection ResultOfMethodCallIgnored
                            zip_file.delete();
                            Log.v("PICTURES "+TAG, ""+zip_file.getAbsolutePath()+" deleted");
                        } catch (Exception ex){
                            Log.e("PICTURES "+ERROR, "Something went wrong");
                            ex.printStackTrace();
                        }
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

        // delete duplicate images on exit
        try {
            for (Uri uri : uris) {
                File file1 = new File(getRealPathFromURI(uri, getActivity()));
                file1.deleteOnExit();
                Log.v("PICTURES "+TAG, ""+file1.getAbsolutePath()+" deleted.");
            }
        } catch (Exception ex) {
            Log.e("PICTURES "+ERROR, "Something went wrong");
            ex.printStackTrace();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save recycler view state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putString(FIRST_NAME,first_name.getText().toString());
        outState.putString(LAST_NAME,last_name.getText().toString());
        outState.putString(EMAIL_ADDRESS, email_address.getText().toString());
        outState.putString(REG_NO,admission_num.getText().toString());
        outState.putString(DEPARTMENT, dept);
        outState.putString(YEAR, yr);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // retrieve contents on screen before rotation
        if (savedInstanceState != null) {
            first_name.setText(savedInstanceState.getString(FIRST_NAME));
            last_name.setText(savedInstanceState.getString(LAST_NAME));
            email_address.setText(savedInstanceState.getString(EMAIL_ADDRESS));
            admission_num.setText(savedInstanceState.getString(REG_NO));
            department.setPrompt(savedInstanceState.getCharSequence(DEPARTMENT));
            years.setPrompt(savedInstanceState.getCharSequence(YEAR));
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    /**
     * Method checks whether information has been entered before submission
     */
    public void updateSubmitButtonState() {
        if ( uris.size() >= 10 && firstNameWatcher.nonEmpty() && regWatcher.nonEmpty()
                && lastNameWatcher.nonEmpty() && emailAddressWatcher.nonEmpty() ) {
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
     * It utilises a class that enables setting of hints to the spinners
     * Courtesy of https://github.com/srodrigo/Android-Hint-Spinner
     */
    private class SelectItem {

        Spinner spinner;
        int hintResource;
        HintSpinner<String> hintSpinner;

        SelectItem(Spinner spinner, int hintResource, List<String> list, final String choice) {
            this.spinner = spinner;
            this.hintResource = hintResource;
            hintSpinner = new HintSpinner<>(
                    this.spinner,
                    // Default layout - You don't need to pass in any layout id, just hint text and
                    // list data
                    new HintAdapter<>(getActivity(), hintResource, list),
                    new HintSpinner.Callback<String>() {
                        @Override
                        public void onItemSelected(int position, String itemAtPosition) {
                            if (choice.equals(YEAR)) yr = itemAtPosition;
                            else if (choice.equals(DEPARTMENT)) dept = itemAtPosition;
                        }
                    });
        }

        HintSpinner<String> getHintSpinner() {
            return hintSpinner;
        }
    }

    List<String> setList(TreeSet<String> stringTreeSet) {
        List<String> list = new ArrayList<>();
        list.addAll(stringTreeSet);
        return list;
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
                        i, uris);
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
            adapter = new GridLayoutAdapter(getActivity(), uris);
            recyclerViewMain.setAdapter(adapter);
        } else {
            adapter.setImages(uris);
            adapter.notifyDataSetChanged();
        }
    }

    private void initializeImages() {
        uris = new ArrayList<>();
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
