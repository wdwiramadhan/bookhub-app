package com.example.bookhub.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class UpdateProductActivity extends AppCompatActivity {
    private EditText etName, etAuthor, etDescription, etPrice;
    private Button btnUpdateProduct;
    private String id, name, author, priceStr, description, image;
    private  int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        Intent intent = getIntent();
        id = intent.getExtras().getString("idProduct");
        etName = findViewById(R.id.et_name_update);
        etAuthor = findViewById(R.id.et_author_update);
        etPrice = findViewById(R.id.et_price_update);
        etDescription = findViewById(R.id.et_description_update);
        btnUpdateProduct = findViewById(R.id.btn_updateProduct);
        getProduct();
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                author = etAuthor.getText().toString();
                priceStr = etPrice.getText().toString();
                description = etDescription.getText().toString();
                if(name.trim().equals("")){
                    etName.setError("Judul harus diisi");
                }else if(author.trim().equals("")){
                    etAuthor.setError("Penulis harus diisi");
                }else if(priceStr.trim().equals("")){
                    etPrice.setError("Harga harus diisi");
                }else if(description.trim().equals("")){
                    etDescription.setError("Deskripsi harus diisi");
                }else{
                    price = Integer.parseInt(priceStr);
                    updateProduct();
                }

            }
        });
    }


    private void getProduct(){
        APIRequestProduct ardProduct = RetroServer.connectRetro().create(APIRequestProduct.class);
        Call<ResponseProductModel> product = ardProduct.ardGetProduct(id);
        product.enqueue(new Callback<ResponseProductModel>() {
            @Override
            public void onResponse(Call<ResponseProductModel> call, Response<ResponseProductModel> response) {
                id = response.body().getData().getId();
                name = response.body().getData().getName();
                author = response.body().getData().getAuthor();
                price = response.body().getData().getPrice();
                description = response.body().getData().getDescription();

                etName.setText(String.valueOf(name));
                etAuthor.setText(String.valueOf(author));
                etPrice.setText(String.valueOf(price));
                etDescription.setText(String.valueOf(description));
            }

            @Override
            public void onFailure(Call<ResponseProductModel> call, Throwable t) {
                Toast.makeText(UpdateProductActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct(){
        APIRequestProduct ardProduct = RetroServer.connectRetro().create(APIRequestProduct.class);
        Call<ResponseProductModel> product = ardProduct.ardUpdateProduct(id, name, author, price, description);
        product.enqueue(new Callback<ResponseProductModel>() {
            @Override
            public void onResponse(Call<ResponseProductModel> call, Response<ResponseProductModel> response) {
                finish();
            }

            @Override
            public void onFailure(Call<ResponseProductModel> call, Throwable t) {
                Toast.makeText(UpdateProductActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}