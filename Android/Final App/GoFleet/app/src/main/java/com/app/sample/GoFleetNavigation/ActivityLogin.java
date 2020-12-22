package com.app.sample.GoFleetNavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.sample.GoFleetNavigation.data.GlobalVariable;
import com.app.sample.GoFleetNavigation.model.AbstractAPIListener;
import com.app.sample.GoFleetNavigation.model.User;
import com.google.mlkit.md.LiveBarcodeScanningActivity;

import java.util.ArrayList;

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

    private Button btnHelp;

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
        btnHelp = (Button) findViewById(R.id.btn_Help);

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                validateForm();
                String email = input_username.getText().toString();
                String password = input_password.getText().toString();
                signIn(email, password);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ActivityLogin.this, ActivityHelp.class);
                intent.putExtra("Login",true);
                startActivity(intent);
                finish();
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


        modelApi.login(email,password, new AbstractAPIListener() //login method
        {
            @Override
            public void onLogin(User user) //when you get a response back execute this
            {
                modelApi.setUser(user); //sets user who just signed
                Toast.makeText(ActivityLogin.this,"Welcome " + user.getName(),Toast.LENGTH_LONG).show();


               Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
               intent.putExtra("User",user.getName().concat(" " + user.getSurname()));
               startActivity(intent);
                //  startActivity(new Intent(ActivityLogin.this, ActivityRouteShowCase.class));
                  finish();
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
