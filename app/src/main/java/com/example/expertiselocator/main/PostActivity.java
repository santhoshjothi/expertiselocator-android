package com.example.expertiselocator.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.example.expertiselocator.model.request.AddPostRequest;
import com.example.expertiselocator.model.request.UserInfoRequest;
import com.example.expertiselocator.model.response.GetUserInfoResponse;
import com.example.expertiselocator.model.response.LoginResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {
    CommonMethods commonMethods;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmapsimg;
    private Button btn_pst_post,btn_pst_imgdel_post;
    ImageView img_camera_post,img_prof_post,img_preview_post,img_link_post,img_toolbar_close_post;
    EditText edt_msg_pst;
    LinearLayout lin_link_box;
    FrameLayout lin_img_post;
    public static final String TAG = PostActivity.class.getSimpleName();
    SharedPreferencesWithAES prefs;
    SharedPreferences.Editor editor;
    UserInfoModelPref userResponse;
    LoginResponse loginResponse ;
    ProgressView progress_post;
    String getToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        prefs = SharedPreferencesWithAES.getInstance(PostActivity.this,"expertise_Prefs"); //provide context & preferences name.
        commonMethods = new CommonMethods(PostActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        img_camera_post=(ImageView)findViewById(R.id.img_camera_post);
        img_prof_post=(ImageView)findViewById(R.id.img_prof_post);
        img_preview_post=(ImageView)findViewById(R.id.img_preview_post);
        btn_pst_post=(Button)findViewById(R.id.btn_pst_post);
        btn_pst_imgdel_post=(Button)findViewById(R.id.btn_pst_imgdel_post);
        edt_msg_pst=(EditText) findViewById(R.id.edt_msg_pst);
        img_link_post=(ImageView) findViewById(R.id.img_link_post);
        //  lin_link_box=(LinearLayout) findViewById(R.id.lin_link_box);
        lin_img_post=(FrameLayout) findViewById(R.id.lin_img_post);
        img_toolbar_close_post=(ImageView) findViewById(R.id.img_toolbar_close_post);
        progress_post = (ProgressView) findViewById(R.id.progress_post);


        try {

            String getUserInfo = prefs.getString("user_info","");
            ObjectMapper mapper = new ObjectMapper();
            //loginResponse = mapper.readValue(prefs.getString("loginResponse",""), LoginResponse.class);
            getToken=prefs.getString("loginResponse","");
            userResponse = mapper.readValue(getUserInfo, UserInfoModelPref.class);
           // Log.v("Timeline_Fragment",""+loginResponse.getToken());
            Log.v("Timeline_Fragment",""+userResponse.getUserId());
            byte[] imageByte = Base64.decode(userResponse.profilepicture, Base64.DEFAULT);
            Glide.with(getApplicationContext()).asBitmap().load(imageByte).into(img_prof_post);

            prefs.putString("postImgPath","");
            prefs.putString("videolink","");
            prefs.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }


        img_camera_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });

        btn_pst_imgdel_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_preview_post.setVisibility(View.GONE);
                btn_pst_imgdel_post.setVisibility(View.GONE);
                prefs.putString("postImgPath","");
                prefs.commit();
            }
        });
        img_toolbar_close_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        img_link_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_img_post.setVisibility(View.GONE);
                prefs.putString("postImgPath","");
                prefs.commit();

                LayoutInflater factory = LayoutInflater.from(PostActivity.this);
                final View deleteDialogView = factory.inflate(R.layout.custom_dialogbox, null);
                final AlertDialog deleteDialog = new AlertDialog.Builder(PostActivity.this).create();
                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                deleteDialog.setCancelable(false);
                deleteDialog.setView(deleteDialogView);
                EditText editText = (EditText) deleteDialogView.findViewById(R.id.edt_box);
                deleteDialogView.findViewById(R.id.btn_submit_box).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //your business logic
                        if(!editText.getText().toString().equalsIgnoreCase("") && !editText.getText().toString().isEmpty()){

                            prefs.putString("videolink", editText.getText().toString());
                            prefs.commit();

                        }
                        deleteDialog.dismiss();
                    }
                });
                deleteDialogView.findViewById(R.id.btn_cancel_box).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                });

                deleteDialog.show();
            }
        });

        btn_pst_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edt_msg_pst.getText().toString().isEmpty()) {

                    String postImgPath = prefs.getString("postImgPath", "");
                    String postVideoLink = prefs.getString("videolink", "");
                    lin_img_post.setVisibility(View.GONE);
                    Log.v(TAG, "postImgPath" + postImgPath);
                    //System.out.print("postImgPath"+postImgPath);
                    Log.v(TAG, "postVideoLink" + postVideoLink);

                    AddPostRequest addPostRequest = new AddPostRequest();
                    addPostRequest.setMessage(edt_msg_pst.getText().toString());
                    addPostRequest.setUserID(Integer.parseInt(userResponse.getUserId()));
                    addPostRequest.setPostedBy(Integer.parseInt(userResponse.getUserId()));
                    addPostRequest.setPostOwnerId(Integer.parseInt(userResponse.getUserId()));
                    addPostRequest.setSharedPostId(0);
                    addPostRequest.setAssestType("");
                    addPostRequest.setIsActive(0);
                    if ((postImgPath == null || postImgPath.trim().equals("")) && (postVideoLink == null || postVideoLink.trim().equals(""))) {


                        commonMethods.showLog("Null Token  : " , TAG + "");
                        addPostRequest.setPostImage("");
                        addPostRequest.setPostVideo("");
                        callAddPost(addPostRequest);

                    } else if ((postImgPath != null || !postImgPath.trim().equals("")) && (postVideoLink == null || postVideoLink.trim().equals(""))) {

                        addPostRequest.setPostImage(postImgPath);
                        addPostRequest.setPostVideo("");
                        callAddPost(addPostRequest);
                    } else if ((postImgPath == null || postImgPath.trim().equals("")) && (postVideoLink != null || !postVideoLink.trim().equals("")))

                        addPostRequest.setPostImage("");
                        addPostRequest.setPostVideo(postVideoLink);
                        callAddPost(addPostRequest);
                }

                else{


                    commonMethods.showToast(getResources().getString(R.string.post_msg));
                }
            }
        });

    }

    public void selectimage(){
        Intent imgintent = new Intent();
        imgintent.setType("image/*");
        imgintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgintent,IMG_REQUEST);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode==RESULT_OK && data != null){
            Uri path = data.getData();

            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(path);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                //ConnectivityReceiver.logMsg(PostActivity.this,TAG,"encodedImage : ",encodedImage,"v");
                bitmapsimg = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                //
                Bitmap bitmap=getResizedBitmap(selectedImage,400);
                String encodedImage = encodeImage(bitmap);
                //System.out.println("encodedImage"+encodedImage);
                prefs.putString("postImgPath",encodedImage);
                prefs.commit();
                img_preview_post.setImageBitmap(bitmapsimg);
                lin_img_post.setVisibility(View.VISIBLE);


                img_preview_post.setVisibility(View.VISIBLE);
                btn_pst_imgdel_post.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String encodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        // Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        byte[] byteArray = baos.toByteArray();
        //Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        Log.v("ImageSize",""+byteArray.length);
        String encImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encImage;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void callAddPost(AddPostRequest userInfo){
        Log.v(TAG, "postImgPath" + getToken);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization(getToken).create(ExpertiseApiInterface.class);
        Call<String> getPostedMessage = apiInterface.getAddPosts(userInfo);
        progress_post.setVisibility(View.VISIBLE);
        getPostedMessage.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                progress_post.setVisibility(View.GONE);
                edt_msg_pst.setText("");
                commonMethods.showLog("URL Success : " , TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG +  response.code());
                commonMethods.showLog("Response Body : " ,TAG + response.body());

                try{

                    if(response.code()==200){

                        if(!response.body().equalsIgnoreCase("0")){

                            commonMethods.showToast(getResources().getString(R.string.pst_success));

                        }else{

                            commonMethods.showToast(getResources().getString(R.string.pst_failure));

                        }

                    }else{

                        commonMethods.showLog("Response code : " ,TAG + response.code());

                    }


                }catch(NullPointerException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_post.setVisibility(View.GONE);
                commonMethods.showLog("URL Failure : " ,TAG + call.request().url());
                commonMethods.showLog("Failure : ",TAG  + t.getMessage());
            }
        });




    }
}
