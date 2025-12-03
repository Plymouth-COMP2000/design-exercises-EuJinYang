package com.example.hungryneat.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryneat.R;
import com.example.hungryneat.Utils;

public class ConfirmPhone extends AppCompatActivity {

    EditText[] codeDigits;
    Button btnVerify;
    TextView tvResend, tvTimer;
    ImageView ivBackArrow;

    String mode, username, phone, password, code;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 120000; // 2 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_phone_layout);

        // Get data from previous screen
        mode = getIntent().getStringExtra("mode");
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone"); // may be null for login
        password = getIntent().getStringExtra("password"); // only for register
        code = getIntent().getStringExtra("code");

        // Initialize 6 digits
        codeDigits = new EditText[]{
                findViewById(R.id.et_digit1),
                findViewById(R.id.et_digit2),
                findViewById(R.id.et_digit3),
                findViewById(R.id.et_digit4),
                findViewById(R.id.et_digit5),
                findViewById(R.id.et_digit6)
        };

        btnVerify = findViewById(R.id.btn_verify);
        tvResend = findViewById(R.id.tv_resend_code);
        tvTimer = findViewById(R.id.tv_timer);
        ivBackArrow = findViewById(R.id.iv_back_arrow);

        setupAutoFocus();
        startTimer();

        // Back arrow functionality
        ivBackArrow.setOnClickListener(v -> {
            if (mode != null && mode.equals("register")) {
                // Go back to register page
                Intent intent = new Intent(ConfirmPhone.this, Register.class);
                startActivity(intent);
            }
            finish();
        });

        Toast.makeText(this, "Verification code sent to " + phone, Toast.LENGTH_SHORT).show();

        btnVerify.setOnClickListener(v -> {
            StringBuilder enteredCode = new StringBuilder();
            for (EditText e : codeDigits) {
                String digit = e.getText().toString().trim();
                if (digit.isEmpty()) {
                    Toast.makeText(this, "Please enter all digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                enteredCode.append(digit);
            }

            if (enteredCode.toString().equals(code)) {
                Toast.makeText(this, "Phone verified successfully!", Toast.LENGTH_SHORT).show();
                // navigate to next screen (register or login)
            } else {
                Toast.makeText(this, "Incorrect verification code", Toast.LENGTH_SHORT).show();
            }
        });

        tvResend.setOnClickListener(v -> {
            if (timerRunning) {
                Toast.makeText(this, "Please wait until timer finishes to resend", Toast.LENGTH_SHORT).show();
                return;
            }

            code = Utils.generateSixDigitCode(); // generate new code
            Toast.makeText(this, "New code sent to " + phone, Toast.LENGTH_SHORT).show();
            clearCodeInputs();
            resetTimer();
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                tvTimer.setText("Time Remaining: 00:00");
                tvResend.setEnabled(true);
            }
        }.start();

        timerRunning = true;
        tvResend.setEnabled(false);
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("Time Remaining: %02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 120000; // Reset to 2 minutes
        startTimer();
    }

    private void setupAutoFocus() {
        for (int i = 0; i < codeDigits.length; i++) {
            final int index = i;
            codeDigits[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < codeDigits.length - 1) {
                        codeDigits[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        codeDigits[index - 1].requestFocus();
                    }
                }
            });
        }
    }

    private void clearCodeInputs() {
        for (EditText e : codeDigits) {
            e.setText("");
        }
        codeDigits[0].requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}