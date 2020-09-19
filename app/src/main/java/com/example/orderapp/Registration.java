package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends Fragment {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;

    EditText edit_name;
    EditText edit_email;
    EditText edit_phone;
    EditText edit_password;
    EditText edit_confirm_password;
    Button button_signup;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    View view;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registration, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor = sharedPreferences.edit();

        edit_name = view.findViewById(R.id.edit_text_signup_name);
        edit_email = view.findViewById(R.id.edit_text_signup_email);
        edit_phone = view.findViewById(R.id.edit_text_signup_phone);
        edit_password = view.findViewById(R.id.edit_text_signup_password);
        edit_confirm_password = view.findViewById(R.id.edit_text_signup_confirm_password);

        button_signup = view.findViewById(R.id.button_signup);

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                name = edit_name.getText().toString();
                email = edit_email.getText().toString();
                phone = edit_phone.getText().toString();
                password = edit_password.getText().toString();
                confirmPassword = edit_confirm_password.getText().toString();

                if (name.length() > 0 && email.length() > 0 && phone.length() > 0 && password.length() > 0 && confirmPassword.length() > 0) {
                    if (password.equals(confirmPassword)) {
                        UserObject userObject = new UserObject(null, name, email, phone, password);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(UrlHelper.BaseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                        Call<UserObject> call = jsonPlaceHolderApi.registration(userObject);

                        call.enqueue(new Callback<UserObject>() {
                            @Override
                            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                                if (response.isSuccessful()) {
                                    UserObject userObject1 = response.body();

                                    editor.putString("user_id", userObject1.getId());
                                    editor.putString("user_name", userObject1.getFullName());
                                    editor.putString("user_email", userObject1.getEmail());
                                    editor.putString("user_phone", userObject1.getPhone());
                                    editor.commit();

                                    Toast.makeText(getContext(), "User sign up successful!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    startActivityForResult(intent, 0);

                                } else {
                                    Toast.makeText(getContext(), "User sign up error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<UserObject> call, Throwable t) {
                                Toast.makeText(getContext(), "User sign up error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill up all the fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}