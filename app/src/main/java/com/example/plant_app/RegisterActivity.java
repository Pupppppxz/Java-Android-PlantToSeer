package com.example.plant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Button loginButton, registerButton;
    private EditText inputEmail, inputPassword, inputPasswordRecheck, inputFirstName, inputLastName;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.reEmail);
        inputPassword = findViewById(R.id.rePassword);
        inputPasswordRecheck = findViewById(R.id.reEnterPassword);
        inputFirstName = findViewById(R.id.reName);
        inputLastName = findViewById(R.id.reLastName);
        loginButton = findViewById(R.id.reSignIn);
        registerButton = findViewById(R.id.reSignUp);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignIn();
            }
        });
    }

    private void PerforAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputPasswordRecheck.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();

        if (firstName.isEmpty()) {
            inputFirstName.setError("Enter Name Field");
        } else if (lastName.isEmpty()) {
            inputLastName.setError("Enter Lastname Field");
        } else if (!email.matches(this.emailPattern)) {
            inputEmail.setError("Enter Connect Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
        } else if (!password.equals(confirmPassword)) {
            inputPasswordRecheck.setError("Password Not Match");
        } else {
            progressDialog.setMessage("Please Wait While Registration..");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void navigateToSignIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}