package com.tarificompany.android_ass1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtPass;
    private Button btnSignUp;
    private TextView txtHaveAccount;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpSharedPref();

        // Check if user is already logged in
        if (isLoggedIn()) {
            Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish(); // Close SignUpActivity
            return;
        }

        setUpViews();
    }

    /**
     * Check if user is already logged in based on SharedPreferences
     */
    private boolean isLoggedIn() {
        boolean flag = pref.getBoolean(LoginActivity.FLAG, false);
        String storedName = pref.getString(LoginActivity.NAME, "");
        String storedPass = pref.getString(LoginActivity.PASS, "");
        return flag && !storedName.isEmpty() && !storedPass.isEmpty();
    }

    /**
     * setUpViews method that will initialise the hooks.
     */
    public void setUpViews() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtHaveAccount = findViewById(R.id.txtHaveAccount);
    }

    /**
     * setUpSharedPref method that will initialise the shared preferences objects.
     */
    public void setUpSharedPref() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
    }

    /**
     * btnSignUpOnClick method that will make actions for the signup button
     */
    public void btnSignUpOnClick(View view) {
        String name = edtUserName.getText().toString();
        String pass = edtPass.getText().toString();

        // Check if fields are empty
        if (name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user already exists in SharedPreferences.
        String storedName = pref.getString(LoginActivity.NAME, "");
        if (name.equals(storedName)) {
            Toast.makeText(this, "This username already exists. Please login or choose another username.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save new user credentials and go to HomeActivity.
        editor.putString(LoginActivity.NAME, name);
        editor.putString(LoginActivity.PASS, pass);
        editor.putBoolean(LoginActivity.FLAG, true); // set flag to true
        editor.commit();

        Toast.makeText(this, "Sign-Up successful!", Toast.LENGTH_SHORT).show();

        // Go to HomeActivity after successful signup.
        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish(); // Prevent back navigation to SignUpActivity
    }

    /**
     * txtHaveAccountOnClick method that will go to the login page.
     */
    public void txtHaveAccountOnClick(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Prevent going back to register screen
    }
}