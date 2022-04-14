package com.example.plant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_STATUS = "status";

    private Button loginButton, registerButton;
    private EditText inputEmail, inputPassword, inputPasswordRecheck, inputFirstName, inputLastName;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(view -> PerforAuth());

        loginButton.setOnClickListener(view -> navigateToSignIn());
    }

    public void saveNewUser(String userId, String email, String password, String firstName, String lastName) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_FIRSTNAME, firstName);
        user.put(KEY_LASTNAME, lastName);
        user.put(KEY_EMAIL, email);
        user.put(KEY_PASSWORD, password);
        user.put(KEY_USER_ID, userId);
        user.put(KEY_STATUS, "user");

        db.collection("User").document(userId).set(user)
                .addOnSuccessListener(unused -> Toast.makeText(RegisterActivity.this, "Created user successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    private void PerforAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputPasswordRecheck.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();

        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}";

        if (firstName.isEmpty()) {
            inputFirstName.setError("Enter Name Field");
        } else if (lastName.isEmpty()) {
            inputLastName.setError("Enter Lastname Field");
        } else if (!email.matches(this.emailPattern)) {
            inputEmail.setError("Enter Connect Email");
        } else if (password.isEmpty()) {
            inputPassword.setError("Enter Password Field");
        } else if (!password.matches(pattern)) {
            inputPassword.setError("Password must have number, uppercase letter and lower case letter");
        } else if (!password.equals(confirmPassword)) {
            inputPasswordRecheck.setError("Password Not Match");
        } else {
            progressDialog.setMessage("Please Wait While Registration..");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = firebaseUser.getUid();
                    progressDialog.dismiss();
                    saveNewUser(userId, email, password, firstName, lastName);
                    sendUserToActivity();
                    Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
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