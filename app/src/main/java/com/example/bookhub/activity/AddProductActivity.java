package com.example.bookhub.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookhub.R;
import com.example.bookhub.api.APIRequestProduct;
import com.example.bookhub.api.RetroServer;
import com.example.bookhub.model.ResponseProductModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private EditText etCode, etName, etAuthor, etDescription, etImage, etPrice;
    private Button btnAddProduct;
    private String code, name, author, priceStr, description;
    private  int price;
    private String image = "https://i.postimg.cc/xTzmQrDR/0-i-Bvb3-FQRn-C4-Xdyv4.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        etCode = findViewById(R.id.et_code);
        etName = findViewById(R.id.et_name);
        etAuthor = findViewById(R.id.et_author);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        etImage = findViewById(R.id.et_image);
        btnAddProduct = findViewById(R.id.btn_addProduct);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = etCode.getText().toString();
                name = etName.getText().toString();
                author = etAuthor.getText().toString();
                priceStr = etPrice.getText().toString();
                description = etDescription.getText().toString();
                image = etImage.getText().toString();
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
                    createProduct();
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
}