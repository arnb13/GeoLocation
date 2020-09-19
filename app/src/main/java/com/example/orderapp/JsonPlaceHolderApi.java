package com.example.orderapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @GET(UrlHelper.ProductApi)
    Call<List<ProductObject>> getProduct();

    @POST(UrlHelper.RegistrationApi)
    Call<UserObject> registration(@Body UserObject userObject);

    @POST(UrlHelper.LoginApi)
    Call<UserObject> login(@Body LoginObject loginObject);

    @POST(UrlHelper.OrderApi)
    Call<OrderObject> order(@Body OrderObject orderObject);

}
