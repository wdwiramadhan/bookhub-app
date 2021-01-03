package com.example.bookhub.api;

import com.example.bookhub.model.ResponseProductModel;
import com.example.bookhub.model.ResponseProductsModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIRequestProduct {
    @GET("product")
    Call<ResponseProductsModel> ardRetrieveProduct();

    @FormUrlEncoded
    @POST("product")
    Call<ResponseProductModel> ardCreateProduct(
            @Field("id") String code,
            @Field("name") String name,
            @Field("author") String author,
            @Field("price") int price,
            @Field("description") String description,
            @Field("image") String image
    );

    @GET("product/{id}")
    Call<ResponseProductModel> ardGetProduct( @Path("id") String id );

    @FormUrlEncoded
    @PUT("product/{id}")
    Call<ResponseProductModel> ardUpdateProduct(
            @Path("id") String id,
            @Field("name") String name,
            @Field("author") String author,
            @Field("price") int price,
            @Field("description") String description
    );

    @DELETE("product/{id}")
    Call<ResponseProductModel> ardDeleteProduct (@Path("id") String id);
}
