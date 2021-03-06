package com.example.android.classlist.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.android.classlist.fragments.MainFragment;

import static com.example.android.classlist.others.Other.Constants.*;

public class MainActivity extends MainFragmentActivity {

    /**
     * Method to called in LoginFragment to create Intent containing extra info as needed.
     * Helps to hide MainActivity's needed extras
     * @param packageContext current context of application
     * @param full_name user's full name
     * @param reg_num user's registration number
     * @param dir Directory to save images
     * @return intent to be created
     */
    public static Intent newIntent(Context packageContext, String full_name, String reg_num,
                                   String dir) {
        Intent i = new Intent(packageContext, MainActivity.class);
        i.putExtra(EXTRA_USER_FULL_NAME, full_name);
        i.putExtra(EXTRA_USER_REG_NUM, reg_num);
        i.putExtra(EXTRA_USER_DIR, dir);
        return i;
    }

    @Override
    protected Fragment createFragment(){
        // retrieve saved key-value intent pair and pass to fragment
        String name = getIntent().getStringExtra(EXTRA_USER_FULL_NAME);
        String reg_num = getIntent().getStringExtra(EXTRA_USER_REG_NUM);
        String dir = getIntent().getStringExtra(EXTRA_USER_DIR);
        return MainFragment.newInstance(name,reg_num,dir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
