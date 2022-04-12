package com.example.plant_app.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.plant_app.HomeActivity;
import com.example.plant_app.R;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String userId;
    
    private static final int PICK_IMAGE_REQUEST = 25;
    private ProgressDialog progressDialog;
    private EditText inputFirstName, inputLastName, inputEmail, inputPassword;
    private ImageButton inputEditImageProfile;
    private Button btnSubmit, btnReset;
    private Uri filePath;
    private User user;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initElement(v);
        inputEditImageProfile.setOnClickListener(view -> SelectImage());

        btnReset.setOnClickListener(view -> sendUserToHome());

        btnSubmit.setOnClickListener(view -> PerformUpdate());

        return v;
    }

    private void PerformUpdate() {

        String firstname = inputFirstName.getText().toString();
        String lastname = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        User newUser = new User(firstname, lastname, email, password, user.getStatus(), user.getUserId());

        updateAuthPassword(email, password);
        updateUserDetail(newUser);

        if (filePath != null) {
            progressDialog.setMessage("Please Wait While Update profile..");
            progressDialog.setTitle("Updated");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StorageReference ref = storageReference
                    .child(FirebaseLocal.storagePathForImageUpload + userId + "/profile");

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast
                                .makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT)
                                .show();
                        sendUserToHome();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast
                                .makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    });
        } else {
            System.out.println("file path = null");
        }
    }

    private void updateUserDetail(User newUser) {
        db.collection("User").document(userId).set(newUser)
                .addOnSuccessListener(unused -> Toast
                        .makeText(getActivity(), "updated", Toast.LENGTH_SHORT)
                        .show())
                .addOnFailureListener(e -> Toast
                        .makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show());
    }

    private void updateAuthPassword(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, user.getPassword());
        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(password)
                                .addOnCompleteListener(task1 -> System.out.println("complete updated password!"));
                    } else {
                        System.out.println("Error to update password");
                    }
                })
                .addOnFailureListener(e -> System.out.println("Error to complete password!"));
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            inputEditImageProfile.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initElement(View v) {
        inputFirstName = v.findViewById(R.id.edit_profile_firstname);
        inputLastName = v.findViewById(R.id.edit_profile_lastname);
        inputEmail = v.findViewById(R.id.edit_profile_email);
        inputPassword = v.findViewById(R.id.edit_profile_password);
        inputEditImageProfile = v.findViewById(R.id.edit_profile_insert_image);
        btnSubmit = v.findViewById(R.id.updateProfileBtnSubmit);
        btnReset = v.findViewById(R.id.updateProfileBtnReset);
        progressDialog = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        inputFirstName.setText(user.getFirstname());
        inputLastName.setText(user.getLastname());
        inputEmail.setText(user.getEmail());
        inputPassword.setText(user.getPassword());
    }

    private void sendUserToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setUser(User user) {
        this.user = user;
    }
}