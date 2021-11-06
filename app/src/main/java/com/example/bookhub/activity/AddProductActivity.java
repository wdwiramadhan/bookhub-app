package com.example.bookhub.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.bookhub.R;
import com.example.bookhub.api.APIRequestProduct;
import com.example.bookhub.api.RetroServer;
import com.example.bookhub.model.ResponseProductModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private EditText etCode, etName, etAuthor, etDescription, etPrice;
    private ImageView ivImage;
    private Button btnAddProduct;
    private String code, name, author, priceStr, description;
    private  int price;
    private String image = "https://i.postimg.cc/xTzmQrDR/0-i-Bvb3-FQRn-C4-Xdyv4.jpg";

    private static final int PERMISSION_CODE =1;
    private static final int PICK_IMAGE=1;

    String filePath;
    Map config = new HashMap();

    private void configCloudinary() {
        config.put("cloud_name", "");
        config.put("api_key", "");
        config.put("api_secret", "");
        MediaManager.init(AddProductActivity.this, config);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        etCode = findViewById(R.id.et_code);
        etName = findViewById(R.id.et_name);
        etAuthor = findViewById(R.id.et_author);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        ivImage = findViewById(R.id.iv_image_add);
        btnAddProduct = findViewById(R.id.btn_addProduct);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddProduct.setEnabled(false);
                btnAddProduct.setClickable(false);
                code = etCode.getText().toString();
                name = etName.getText().toString();
                author = etAuthor.getText().toString();
                priceStr = etPrice.getText().toString();
                description = etDescription.getText().toString();
                if(code.trim().equals("")){
                    etCode.setError("Code harus diisi");
                }else if(name.trim().equals("")){
                    etName.setError("Judul harus diisi");
                }else if(author.trim().equals("")){
                    etAuthor.setError("Penulis harus diisi");
                }else if(priceStr.trim().equals("")){
                    etPrice.setError("Harga harus diisi");
                }else if(description.trim().equals("")){
                    etDescription.setError("Deskripsi harus diisi");
                }else{
                    price = Integer.parseInt(priceStr);
                    uploadToCloudinary(filePath);
                }
            }


        });
    }

    private void createProduct(){
        APIRequestProduct ardProduct = RetroServer.connectRetro().create(APIRequestProduct.class);
        Call<ResponseProductModel> createProduct = ardProduct.ardCreateProduct(code, name, author, price, description, image);
        createProduct.enqueue(new Callback<ResponseProductModel>() {
            @Override
            public void onResponse(Call<ResponseProductModel> call, Response<ResponseProductModel> response) {
                finish();
            }

            @Override
            public void onFailure(Call<ResponseProductModel> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission
                (AddProductActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ){

            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    AddProductActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            }else {
                Toast.makeText(AddProductActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void accessTheGallery(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get the image's file location
        filePath = getRealPathFromUri(data.getData(), AddProductActivity.this);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            try {
                //set picked image to the mProfile
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ivImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity){
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if(cursor==null) {
            return imageUri.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public void uploadToCloudinary(String filePath) {
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Toast.makeText(AddProductActivity.this, "Loading", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                image = resultData.get("url").toString();
                createProduct();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }

}
