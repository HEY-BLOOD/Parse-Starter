package com.example.parsestarter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpButton;
    private TextInputEditText usernameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText passwordConfirmInput;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        signUpButton = findViewById(R.id.signup_button);
        usernameInput = findViewById(R.id.username_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        passwordConfirmInput = findViewById(R.id.password_confirm_input);

        signUpButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String email = emailInput.getText().toString();
            String password_confirm = passwordConfirmInput.getText().toString();
            if (password.equals(password_confirm) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email)) {
                signUp(username, password, email);
            } else {
                Toast.makeText(this, "Make sure that the values you entered are correct.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signUp(String username, String password, String email) {
        progressDialog.show();
        ParseUser user = new ParseUser();
        // Set the user's username and password, which can be obtained by a forms
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(e -> {
            progressDialog.dismiss();
            if (e == null) {
                ParseUser.logOut();
                showAlert("Account Created Successfully!", "Please verify your email before Login", false);
            } else {
                ParseUser.logOut();
                showAlert("Error Account Creation failed", "Account could not be created" + " :" + e.getMessage(), true);
            }
        });
    }

    private void showAlert(String title, String message, boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        if (!error) {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}