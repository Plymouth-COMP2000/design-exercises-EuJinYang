package com.example.hungryneat.guest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hungryneat.R;

public class G_Profile_My_Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private View imageOverlay;
    private ImageView cameraIcon;
    private ImageView backArrow;
    private TextView usernameText, phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_my_profile_layout);

        initializeViews();
        setupClickListeners();
        setupProfileImageHoverEffect();
        loadUserData();
        setupBottomNavigation();
    }

    private void initializeViews() {
        backArrow = findViewById(R.id.iv_back_arrow);
        profileImage = findViewById(R.id.profile_image);
        imageOverlay = findViewById(R.id.image_overlay);
        cameraIcon = findViewById(R.id.camera_icon);
        usernameText = findViewById(R.id.username_text);
        phoneText = findViewById(R.id.phone_text);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupProfileImageHoverEffect() {
        // Set touch listener for the entire profile image area
        profileImage.setOnTouchListener((v, event) -> {
            handleHoverEffect(event.getAction());
            return false;
        });

        // Also set touch listener for the overlay to handle exit
        imageOverlay.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                hideHoverEffect();
            }
            return true;
        });

        // Set hover listener for emulator/mouse support
        profileImage.setOnHoverListener((v, event) -> {
            handleHoverEffect(event.getAction());
            return false;
        });

        // Click listener for profile image (triggers image picker)
        View.OnClickListener imagePickerClickListener = v -> openImagePicker();
        profileImage.setOnClickListener(imagePickerClickListener);
        cameraIcon.setOnClickListener(imagePickerClickListener);
    }

    private void handleHoverEffect(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_HOVER_ENTER:
            case MotionEvent.ACTION_MOVE:
                showHoverEffect();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_HOVER_EXIT:
                hideHoverEffect();
                break;
        }
    }

    private void showHoverEffect() {
        imageOverlay.setVisibility(View.VISIBLE);
        cameraIcon.setVisibility(View.VISIBLE);
    }

    private void hideHoverEffect() {
        imageOverlay.setVisibility(View.INVISIBLE);
        cameraIcon.setVisibility(View.INVISIBLE);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                profileImage.setImageURI(selectedImageUri);
                saveProfileImageUri(selectedImageUri.toString());
                Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show();
            }
        }
        hideHoverEffect();
    }

    private void saveProfileImageUri(String imageUri) {
        getSharedPreferences("user_profile", MODE_PRIVATE)
                .edit()
                .putString("profile_image_uri", imageUri)
                .apply();
    }

    private void setupClickListeners() {
        backArrow.setOnClickListener(v -> onBackPressed());

        Button saveChangesBtn = findViewById(R.id.save_changes_btn);
        saveChangesBtn.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserData() {
        // Load saved profile image
        String savedImageUri = getSharedPreferences("user_profile", MODE_PRIVATE)
                .getString("profile_image_uri", null);

        if (savedImageUri != null) {
            profileImage.setImageURI(Uri.parse(savedImageUri));
        }

        // Load saved user data
        String savedUsername = getSharedPreferences("user_profile", MODE_PRIVATE)
                .getString("username", "Guest User");
        String savedPhone = getSharedPreferences("user_profile", MODE_PRIVATE)
                .getString("phone", "");

        usernameText.setText(savedUsername);
        phoneText.setText(savedPhone);
    }

    private void saveProfileChanges() {
        String newUsername = usernameText.getText().toString().trim();
        String newPhone = phoneText.getText().toString().trim();

        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to shared preferences
        getSharedPreferences("user_profile", MODE_PRIVATE)
                .edit()
                .putString("username", newUsername)
                .putString("phone", newPhone)
                .apply();

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Set result to indicate profile was updated
        setResult(RESULT_OK);
    }

    private void setupBottomNavigation() {
        Button reservationBtn = findViewById(R.id.reservation_btn);
        Button menuBtn = findViewById(R.id.menu_btn);
        Button profileBtn = findViewById(R.id.profile_btn);

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Profile_My_Profile.this, G_Reservation.class);
                startActivity(intent);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Profile_My_Profile.this, G_Menu.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(v -> {
            refreshProfile();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideHoverEffect();
    }

    private void refreshProfile() {
        loadUserData();
    }
}