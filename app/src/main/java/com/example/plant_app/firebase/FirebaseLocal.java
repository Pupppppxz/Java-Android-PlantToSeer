package com.example.plant_app.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseLocal {

    private static FirebaseAuth firebaseAuth;
    private static DocumentReference docRef;
    private static FirebaseUser firebaseUser;
    private static FirebaseFirestore db;
    public static final String storagePathForImageUpload = "image_uploads/";
    public static final String databasePathForImageUpload = "All_Image_Uploads_Database";
    FirebaseLocal() {

    }

    public static void logout() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

    public static FirebaseUser checkIsUserIn() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser;
    }

    public static String getUserId() {
        firebaseAuth = FirebaseAuth.getInstance();
        return firebaseUser.getUid();
    }
}
