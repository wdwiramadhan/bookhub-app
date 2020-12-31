package com.example.bookhub.api;

import com.example.bookhub.model.ResponseProductModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIRequestModel {
    @GET("product")
    Call<ResponseProductModel> ardRetrieveProduct();
}
