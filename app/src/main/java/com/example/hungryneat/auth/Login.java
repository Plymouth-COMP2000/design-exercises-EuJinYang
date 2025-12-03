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
import com.example.hungryneat.guest.G_Menu;

public class Login extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLetsEat;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLetsEat = findViewById(R.id.btn_lets_eat);
        tvRegister = findViewById(R.id.tv_no_account);

        tvRegister.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));

        btnLetsEat.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate verification code
            String code = Utils.generateSixDigitCode();

            // Launch phone verification screen
            Intent i = new Intent(Login.this, G_Menu.class); //ConfirmPhone.class
            i.putExtra("mode", "login");
            i.putExtra("username", username); // using phone as username for login
            i.putExtra("code", code);
            startActivity(i);
        });
    }
}