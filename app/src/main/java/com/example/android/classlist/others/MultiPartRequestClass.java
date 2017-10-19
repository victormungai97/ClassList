package com.example.android.classlist.others;

import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.android.classlist.others.Other.Constants.*;

/**
 * Created by victor on 10/18/17.
 * This class carries out requests using ok http client library
 * Courtesy of https://developerandroidguide.blogspot.co.ke/2017/05/upload-image-using-okhttp.html
 * and https://github.com/miskoajkula/Large-File-upload/
 */

class MultiPartRequestClass {
    private MultipartBody.Builder multipartBody;
    private OkHttpClient OkHttpClient;

    MultiPartRequestClass(){
        this.multipartBody = new MultipartBody.Builder();
        this.multipartBody.setType(MultipartBody.FORM);
        this.OkHttpClient = new OkHttpClient();
    }

    /**
     * Add a value and its tag to the multipart request form
     * @param name Tag of the value
     * @param value Value to be sent
     */
    void addObject(String name, Object value){
        if (value != null)
            this.multipartBody.addFormDataPart(name, String.valueOf(value));
    }

    /**
     * Add a file and its tag to the multipart request form
     * @param name Tag of the file
     * @param file File to be sent
     */
    void addFile(String name, File file){
        if (file != null) {
            String file_path = file.getAbsolutePath();
            this.multipartBody.addFormDataPart(name, /* tag for file */
                    file_path.substring(file_path.lastIndexOf('/') + 1), /* file name */
                    createRequestBody(file) /* Request Body */);
        }
    }

    /**
     * Method builds the multipart form,
     * creates and carries out a network request
     * and parses the network response,
     * converting it into a JSONObject for further processing
     * @param url Link to send the form to
     * @return JSONObject form of response
     */
    JSONObject execute(String url){
        // response
        JSONObject jsonObject = new JSONObject();
        // create network request with request builder parse
        Request request = new Request.Builder()
                .url(url)
                .post(this.multipartBody.build())
                .build();
        Log.v("REQUEST "+TAG, ""+request);

        // create new response instance to execute network request
        try {
            Response response = OkHttpClient.newCall(request).execute();
            Log.v("RESPONSE "+TAG, ""+response);

            // show error if response was unsuccessful
            if (!response.isSuccessful()){
                throw new IOException("OkHttpClient "+ERROR+":"+response);
            }

            // response code, if needed
            /* int code = response.networkResponse().code(); */

            if (response.isSuccessful()){
                jsonObject = new JSONObject(response.body().string());
            }

        } catch (Exception ex) {
            Log.e("OkHttpClient "+ERROR, "Something went wrong");
            ex.printStackTrace();
        } finally {
            // free up resources
            multipartBody = null;
            if (OkHttpClient != null) OkHttpClient = null;
        }

        return jsonObject;
    }

    /**
     * Method creates a RequestBody that'll be used when adding files into multi part data form
     * @param file File to be added to form for sending
     * @return Request body
     */
    private static RequestBody createRequestBody(File file){
        // get path of file
        String path = file.getPath();

        // get file extension
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        // get mime(content) type using passed extension
        String content_type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        // create request body to be used in multipart form network request
        return RequestBody.create(MediaType.parse(content_type), file);
    }
}
