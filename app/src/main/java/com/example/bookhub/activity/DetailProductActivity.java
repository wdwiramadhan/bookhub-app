package com.example.bookhub.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookhub.R;
import com.example.bookhub.api.APIRequestProduct;
import com.example.bookhub.api.RetroServer;
import com.example.bookhub.model.ResponseProductModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    ImageView ivImageDetail;
    TextView tvNameDetail, tvAuthorDetail, tvPriceDetail, tvDescriptionDetail;
    private String id, name, author, description, image;
    private  int price;
    Button btnDeleteProduct, btnUpdateProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Intent intent = getIntent();
        id = intent.getExtras().getString("idProduct");

        ivImageDetail = findViewById(R.id.iv_image_detail);
        tvNameDetail = findViewById(R.id.tv_name_detail);
        tvAuthorDetail = findViewById(R.id.tv_author_detail);
        tvPriceDetail = findViewById(R.id.tv_price_detail);
        tvDescriptionDetail = findViewById(R.id.tv_description_detail);
        btnDeleteProduct = findViewById(R.id.btn_delete_product);
        btnUpdateProduct = findViewById(R.id.btn_update_product);

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailProductActivity.this, UpdateProductActivity.class);
                i.putExtra("idProduct", id);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        getProduct();
    }

    private void getProduct(){
        APIRequestProduct ardProduct = RetroServer.connectRetro().create(APIRequestProduct.class);
        Call<ResponseProductModel> product = ardProduct.ardGetProduct(id);
        product.enqueue(new Callback<ResponseProductModel>() {
            @Override
            public void onResponse(Call<ResponseProductModel> call, Response<ResponseProductModel> response) {
                image = response.body().getData().getImage();
                id = response.body().getData().getId();
                name = response.body().getData().getName();
                author = response.body().getData().getAuthor();
                price = response.body().getData().getPrice();
                description = response.body().getData().getDescription();

                Picasso.get().load(image).into(ivImageDetail);
                tvNameDetail.setText(String.valueOf(name));
                tvAuthorDetail.setText(String.valueOf(author));
                tvPriceDetail.setText("Rp. "+String.valueOf(price));
                tvDescriptionDetail.setText(String.valueOf(description));
            }

            @Override
            public void onFailure(Call<ResponseProductModel> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(){
        APIRequestProduct ardProduct = RetroServer.connectRetro().create(APIRequestProduct.class);
        Call<ResponseProductModel> delProduct = ardProduct.ardDeleteProduct(id);
        delProduct.enqueue(new Callback<ResponseProductModel>() {
            @Override
            public void onResponse(Call<ResponseProductModel> call, Response<ResponseProductModel> response) {
                finish();
            }

            @Override
            public void onFailure(Call<ResponseProductModel> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}