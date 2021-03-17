package com.codepath.example.instagramclone;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton, signupButton;
    public static final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goToMainActivity();
        }

        usernameEditText = findViewById(R.id.SignUpUsername);
        passwordEditText = findViewById(R.id.SignUpPassword);
        loginButton = findViewById(R.id.btnLogin);
        signupButton = findViewById(R.id.btnSignUp);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        handleTextUpdates();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }

        });
    }

    private void signUpUser(String username, String password){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(e -> {
           if(e == null){
               Toast.makeText(this, "Successfully signed Up", Toast.LENGTH_SHORT).show();
               loginUser(username, password);
           }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    //todo: error handling for this, let the user know what's going on
                    Log.e(TAG, "done: issue with login", e);
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void handleTextUpdates(){
        //this function will handle text updates from usernameEditText and passwordEditText
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleButton();
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleButton();
            }
        });
    }
    public void handleButton(){
        if(usernameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0){
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

}