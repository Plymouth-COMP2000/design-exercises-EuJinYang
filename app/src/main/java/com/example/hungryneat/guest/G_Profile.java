package com.example.hungryneat.guest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryneat.R;
import com.example.hungryneat.auth.Login;

public class G_Profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_profile_layout);

        initializeViews();
        setupBottomNavigation();
        loadUserData();
        setupProfileOptions();
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profile_image);
        usernameText = findViewById(R.id.username_text);
    }

    private void loadUserData() {
        // Load saved profile image
        String savedImageUri = getSharedPreferences("user_profile", MODE_PRIVATE)
                .getString("profile_image_uri", null);

        if (savedImageUri != null) {
            profileImage.setImageURI(Uri.parse(savedImageUri));
        }

        // Load user data
        String savedUsername = getSharedPreferences("user_profile", MODE_PRIVATE)
                .getString("username", "Guest User");
        usernameText.setText(savedUsername);
    }

    private void setupProfileOptions() {
        // Notification option
        findViewById(R.id.notification_card).setOnClickListener(v -> showNotificationDialog());

        // My Profile option
        findViewById(R.id.my_profile_card).setOnClickListener(v -> {
            Intent intent = new Intent(G_Profile.this, G_Profile_My_Profile.class);
            startActivityForResult(intent, 1); // Use startActivityForResult to refresh when returning
        });

        // Logout option
        findViewById(R.id.logout_card).setOnClickListener(v -> logoutUser());
    }

    private void showNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the dialog layout
        final View dialogView = getLayoutInflater().inflate(R.layout.g_profile_notification_layout, null);
        builder.setView(dialogView);

        // Setup switches
        SwitchCompat switchConfirmReservation = dialogView.findViewById(R.id.switch_confirm_new_reservation);
        SwitchCompat switchReservationChanges = dialogView.findViewById(R.id.switch_reservation_changes);
        SwitchCompat switchReservationCancellations = dialogView.findViewById(R.id.switch_reservation_cancellations);

        // Setup close button
        Button closeButton = dialogView.findViewById(R.id.close_button);

        // Load saved preferences
        switchConfirmReservation.setChecked(getSharedPreferences("notification_settings", MODE_PRIVATE)
                .getBoolean("confirm_new_reservation", true));
        switchReservationChanges.setChecked(getSharedPreferences("notification_settings", MODE_PRIVATE)
                .getBoolean("reservation_changes", true));
        switchReservationCancellations.setChecked(getSharedPreferences("notification_settings", MODE_PRIVATE)
                .getBoolean("reservation_cancellations", true));

        // Use a single listener for all switches
        CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
            String key = "";
            if (buttonView.getId() == R.id.switch_confirm_new_reservation) {
                key = "confirm_new_reservation";
            } else if (buttonView.getId() == R.id.switch_reservation_changes) {
                key = "reservation_changes";
            } else if (buttonView.getId() == R.id.switch_reservation_cancellations) {
                key = "reservation_cancellations";
            }

            if (!key.isEmpty()) {
                getSharedPreferences("notification_settings", MODE_PRIVATE)
                        .edit()
                        .putBoolean(key, isChecked)
                        .apply();
            }
        };

        switchConfirmReservation.setOnCheckedChangeListener(switchListener);
        switchReservationChanges.setOnCheckedChangeListener(switchListener);
        switchReservationCancellations.setOnCheckedChangeListener(switchListener);

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Set close button click listener
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        // Clear user session/data
        getSharedPreferences("user_profile", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        getSharedPreferences("notification_settings", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Redirect to login page
        Intent intent = new Intent(G_Profile.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        Button reservationBtn = findViewById(R.id.reservation_btn);
        Button menuBtn = findViewById(R.id.menu_btn);
        Button profileBtn = findViewById(R.id.profile_btn);

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Profile.this, G_Reservation.class);
                startActivity(intent);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Profile.this, G_Menu.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(v -> {
            // Refresh profile data
            refreshProfile();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Refresh profile data when returning from My Profile activity
        if (requestCode == 1) {
            refreshProfile();
        }
    }

    private void refreshProfile() {
        loadUserData();
    }
}