package com.example.hungryneat.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryneat.R;

public class WelcomePagerAdapter extends RecyclerView.Adapter<WelcomePagerAdapter.VH> {

    private final WelcomeSlide[] slides;

    public WelcomePagerAdapter(WelcomeSlide[] slides) {
        this.slides = slides;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_welcome_slide_layout, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        WelcomeSlide s = slides[position];
        holder.title.setText(s.title);
        holder.subtitle.setText(s.subtitle);
        holder.image.setImageResource(s.imageRes);
    }

    @Override
    public int getItemCount() {
        return slides.length;
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, subtitle;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slide_image);
            title = itemView.findViewById(R.id.slide_title);
            subtitle = itemView.findViewById(R.id.slide_sub);
        }
    }
}