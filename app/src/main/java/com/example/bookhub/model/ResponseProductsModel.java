package com.example.bookhub.model;

import java.util.List;

public class ResponseProductsModel {
    private Boolean success;
    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }

    public void setData(List<ProductModel> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
