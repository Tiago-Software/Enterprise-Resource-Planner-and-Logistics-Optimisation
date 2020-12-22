package com.app.sample.insta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.sample.insta.data.GlobalVariable;
import com.app.sample.insta.model.AbstractAPIListener;
import com.app.sample.insta.model.LottoTicket;
import com.app.sample.insta.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley Wienand, Tiago Pinto, Daniel Dos Santos on 30,01,2020
 */
public class ActivityLogin extends AppCompatActivity
{

    public static final String TAG = "ActivityLogin";
    private EditText input_username;
    private EditText input_password;
    private Button btnSignUp;
    private View parent_view;

    private GlobalVariable globalVariable;

    private ArrayList<ModelApi> mExampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);

        hideKeyboard();

        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);

        input_username.setText("driver@phaki.co.za");
        input_password.setText("1234");

        btnSignUp = (Button) findViewById(R.id.btn_signin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validateForm();
                String email = input_username.getText().toString();
                String password = input_password.getText().toString();
                signIn(email, password);
            }
        });

    }
    public void onStart() {
        super.onStart();
        // User must login for session to start
    }

    private void signIn(String email, String password)
    {
         final ModelApi modelApi = ModelApi.getInstance(ActivityLogin.this.getApplication()); //ref to app obj, must not change when created

        final String[] temp = {""};

        modelApi.login(email,password, new AbstractAPIListener() //login method
        {
            @Override
            public void onLogin(User user) //when you get a response back execute this
            {
                modelApi.setUser(user); //sets user who just signed
                Toast.makeText(ActivityLogin.this,"User " + user.getName(),Toast.LENGTH_LONG).show();

                temp[0] = modelApi.getUser().getName().concat(" " + modelApi.getUser().getSurname());

                Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                intent.putExtra("Userobj",temp[0]); //passes objects
                startActivity(intent);
            }

        });
    }


    /* Form validations */
    private boolean validateForm() {
        boolean valid = true;

        String email = input_username.getText().toString();
        if (TextUtils.isEmpty(email)) {
            input_username.setError("Required.");
            valid = false;
        } else {
            input_username.setError(null);
        }

        String password = input_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            input_password.setError("Required.");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
/*    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
        }else {
            // TODO: add ui configurations
        }
    }*/
}
