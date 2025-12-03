package com.example.hungryneat.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryneat.R;

public class Success extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_layout);

        // After success, redirect to Login
        findViewById(R.id.success_root).setOnClickListener(v -> {
            startActivity(new Intent(Success.this, Login.class));
            finish();
        });
    }
}