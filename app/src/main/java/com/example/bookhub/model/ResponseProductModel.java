package com.example.bookhub.model;

public class ResponseProductModel {
    private Boolean success;
    private ProductModel data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ProductModel getData() {
        return data;
    }

    public void setData(ProductModel data) {
        this.data = data;
    }
}
