package com.example.hungryneat.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hungryneat.R;
import com.example.hungryneat.Utils;
import com.example.hungryneat.auth.Login;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Welcome extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabDots;
    private Button btnGetStarted;
    private Handler autoHandler;
    private final long AUTO_DELAY_MS = 4000;
    private WelcomePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if not first run, go straight to Login
        if (!Utils.isFirstRun(this)) {
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        setContentView(R.layout.welcome_layout);
        viewPager = findViewById(R.id.viewPager);
        tabDots = findViewById(R.id.tabDots);
        btnGetStarted = findViewById(R.id.btn_get_started);

        adapter = new WelcomePagerAdapter(getWelcomeSlides());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabDots, viewPager,
                (tab, position) -> { /* just use tabs as dots */ }
        ).attach();

        // Enable button only on last page
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                btnGetStarted.setEnabled(position == adapter.getItemCount() - 1);
            }
        });

        btnGetStarted.setOnClickListener(v -> {
            Utils.setFirstRunDone(Welcome.this);
            startActivity(new Intent(Welcome.this, Login.class));
            finish();
        });

        // Auto-scroll
        autoHandler = new Handler(Looper.getMainLooper());
        startAutoScroll();
    }

    private void startAutoScroll() {
        autoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int next = (viewPager.getCurrentItem() + 1) % adapter.getItemCount();
                viewPager.setCurrentItem(next, true);
                autoHandler.postDelayed(this, AUTO_DELAY_MS);
            }
        }, AUTO_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoHandler != null) autoHandler.removeCallbacksAndMessages(null);
    }

    private WelcomeSlide[] getWelcomeSlides() {
        return new WelcomeSlide[] {
                new WelcomeSlide(R.drawable.img_welcome_slide1, "Welcome", "Here's a good place for ordering and enjoy your day."),
                new WelcomeSlide(R.drawable.img_welcome_slide2, "Discover", "Find tasty foods and great services."),
                new WelcomeSlide(R.drawable.img_welcome_slide3, "Fast Delivery", "Get your food quickly and hot.")
        };
    }
}