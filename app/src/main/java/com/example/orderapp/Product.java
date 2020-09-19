package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Product extends Fragment implements RecyclerAdapterProduct.OnProductListener {
    private ArrayList<ProductObject> arrayList_product = new ArrayList<>();

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    View view;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlHelper.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<ProductObject>> call = jsonPlaceHolderApi.getProduct();

        call.enqueue(new Callback<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                if (response.isSuccessful()) {
                    List<ProductObject> productObjects = response.body();
                    arrayList_product.clear();

                    for (ProductObject productObject : productObjects) {
                        arrayList_product.add(productObject);
                    }
                    imitProductView(view);

                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void imitProductView(View v) {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView_product);
        RecyclerAdapterProduct adapter = new RecyclerAdapterProduct(arrayList_product, this, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void OnProductClick(int position) {
        ((MainActivity)getActivity()).gpsOnFunction();
        Bundle bundle = new Bundle();
        bundle.putString("product_name", arrayList_product.get(position).getProductName());
        bundle.putString("product_id", arrayList_product.get(position).getId());

        Order fragment = new Order();
        fragment.setArguments(bundle);

        this.getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("tag").commit();


    }
}