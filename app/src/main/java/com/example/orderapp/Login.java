package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class Login extends Fragment {
    private String email;
    private String password;

    EditText edit_email;
    EditText edit_password;
    Button button_login;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    View view;

    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor = sharedPreferences.edit();

        edit_email = view.findViewById(R.id.edit_text_login_email);
        edit_password = view.findViewById(R.id.edit_text_login_password);

        button_login = view.findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                email = edit_email.getText().toString();
                password = edit_password.getText().toString();

                if (email.length() > 0 && password.length() > 0) {
                    LoginObject loginObject = new LoginObject(email, password);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(UrlHelper.BaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                    Call<UserObject> call = jsonPlaceHolderApi.login(loginObject);

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
                                Toast.makeText(getContext(), "Email or Password do not match!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserObject> call, Throwable t) {
                            Toast.makeText(getContext(), "Login failed!", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Please fill up all the fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}