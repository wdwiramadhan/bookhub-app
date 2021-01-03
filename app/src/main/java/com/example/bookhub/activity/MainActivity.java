package com.example.bookhub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookhub.R;
import com.example.bookhub.api.APIRequestProduct;
import com.example.bookhub.adapter.AdapterProduct;
import com.example.bookhub.api.RetroServer;
import com.example.bookhub.model.ProductModel;
import com.example.bookhub.model.ResponseProductsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvProduct;
    private RecyclerView.Adapter adProduct;
    private RecyclerView.LayoutManager lmProduct;
    private List<ProductModel> listProduct;
    private SwipeRefreshLayout srlProduct;
    private ProgressBar pbProduct;
    private FloatingActionButton fabAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvProduct = findViewById(R.id.rv_products);
        srlProduct = findViewById(R.id.srl_product);
        pbProduct = findViewById(R.id.pb_product);
        fabAddProduct = findViewById(R.id.fab_addProduct);
        lmProduct = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(lmProduct);

        srlProduct.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlProduct.setRefreshing(true);
                retrieveProduct();
                srlProduct.setRefreshing(false);
            }
        });

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddProductActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveProduct();
    }

    public void retrieveProduct(){
        pbProduct.setVisibility(View.VISIBLE);
        APIRequestProduct ardProduct = RetroServer.connectRetro().create(APIRequestProduct.class);
        Call<ResponseProductsModel> showProduct = ardProduct.ardRetrieveProduct();
        showProduct.enqueue(new Callback<ResponseProductsModel>() {
            @Override
            public void onResponse(Call<ResponseProductsModel> call, Response<ResponseProductsModel> response) {
                listProduct = response.body().getData();
                adProduct = new AdapterProduct(MainActivity.this, listProduct);
                rvProduct.setAdapter(adProduct);
                adProduct.notifyDataSetChanged();
                pbProduct.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseProductsModel> call, Throwable t) {
                pbProduct.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}