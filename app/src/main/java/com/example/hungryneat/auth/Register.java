package com.example.hungryneat.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryneat.R;
import com.example.hungryneat.Utils;

public class Register extends AppCompatActivity {
    EditText etUsername, etPhone, etPassword, etConfirm;
    Button btnImHungry;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        etUsername = findViewById(R.id.et_username);
        etPhone = findViewById(R.id.et_register_phone_number);
        etPassword = findViewById(R.id.et_register_password);
        etConfirm = findViewById(R.id.et_register_confirm_password);
        btnImHungry = findViewById(R.id.btn_im_hungry);
        tvLogin = findViewById(R.id.tv_have_account);

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });

        btnImHungry.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String confirm = etConfirm.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) ||
                    TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirm)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate verification code
            String code = Utils.generateSixDigitCode();

            // Launch phone verification screen
            Intent i = new Intent(Register.this, ConfirmPhone.class);
            i.putExtra("mode", "register");
            i.putExtra("username", username);
            i.putExtra("phone", phone);
            i.putExtra("password", pass);
            i.putExtra("code", code);
            startActivity(i);
        });
    }
}