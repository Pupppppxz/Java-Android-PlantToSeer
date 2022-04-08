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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
//    private EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    private ImageButton inputEditImageProfile;
    private Button btnSubmit, btnReset;
    private Uri filePath;

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
//        String name = inputName.getText().toString();
//        String email = inputEmail.getText().toString();
//        String password = inputPassword.getText().toString();
//        String confirmPassword = inputConfirmPassword.getText().toString();

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
//        inputName = v.findViewById(R.id.edit_profile_name);
//        inputEmail = v.findViewById(R.id.edit_profile_email);
//        inputPassword = v.findViewById(R.id.edit_profile_password);
//        inputConfirmPassword = v.findViewById(R.id.edit_profile_confirm_password);
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
    }

//    private void clearForm() {
//        inputName.setText("");
//        inputEmail.setText("");
//        inputPassword.setText("");
//        inputConfirmPassword.setText("");
//    }

    private void sendUserToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}