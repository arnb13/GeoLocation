package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Order extends Fragment {
    private String productName;
    private String productId;
    private String userId;
    private int quantity;
    private String address;
    private String latitude;
    private String longitude;

    TextView text_login_warning;
    TextView text_product_name;
    EditText editText_quantity;
    Button submit;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    View view;

    JsonPlaceHolderApi jsonPlaceHolderApi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor = sharedPreferences.edit();

        userId = sharedPreferences.getString("user_id", "");



        productName = getArguments().getString("product_name");
        productId = getArguments().getString("product_id");

        text_product_name = (TextView) view.findViewById(R.id.text_product_name);
        editText_quantity = (EditText) view.findViewById(R.id.editText_quantity);
        submit = (Button) view.findViewById(R.id.button_order_submit);
        text_login_warning = (TextView) view.findViewById(R.id.text_login_warning);

        text_product_name.setText(productName);

        if (userId.length() > 0) {
            submit.setVisibility(View.VISIBLE);
            editText_quantity.setVisibility(View.VISIBLE);
            text_login_warning.setVisibility(View.GONE);

        } else {
            submit.setVisibility(View.GONE);
            editText_quantity.setVisibility(View.GONE);
            text_login_warning.setVisibility(View.VISIBLE);

        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_quantity.getText().toString().length() > 0) {
                    quantity = Integer.parseInt(editText_quantity.getText().toString());
                    latitude = sharedPreferences.getString("lat", "");
                    longitude = sharedPreferences.getString("lon", "");
                    address = sharedPreferences.getString("address", "");

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (quantity > 0) {

                        OrderObject orderObject = new OrderObject(userId, productId, "" + quantity, address, latitude, longitude);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(UrlHelper.BaseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                        Call<OrderObject> call = jsonPlaceHolderApi.order(orderObject);

                        call.enqueue(new Callback<OrderObject>() {
                            @Override
                            public void onResponse(Call<OrderObject> call, Response<OrderObject> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();


                                } else {
                                    Toast.makeText(getContext(), "Order did not placed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderObject> call, Throwable t) {
                                Toast.makeText(getContext(), "Order did not placed", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}