package com.example.hungryneat.guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hungryneat.R;
import java.util.Arrays;
import java.util.List;

public class G_Menu extends AppCompatActivity {

    private LinearLayout foodListLayout;
    private LinearLayout categoriesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_menu_layout);

        setupBottomNavigation();
        initializeViews();
        setupCategoryButtons();
        populateFoodList();
    }

    private void initializeViews() {
        foodListLayout = findViewById(R.id.food_list_layout);
        categoriesLayout = findViewById(R.id.categories_layout);
    }

    private void setupCategoryButtons() {
        // Set up category button click listeners
        Button categoryAll = findViewById(R.id.category_all);
        Button categoryDrinks = findViewById(R.id.category_drinks);
        Button categoryNoodles = findViewById(R.id.category_noodles);
        Button categoryRice = findViewById(R.id.category_rice);
        Button categoryDesserts = findViewById(R.id.category_desserts);

        View.OnClickListener categoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFoodByCategory(((Button) v).getText().toString());
            }
        };

        categoryAll.setOnClickListener(categoryClickListener);
        categoryDrinks.setOnClickListener(categoryClickListener);
        categoryNoodles.setOnClickListener(categoryClickListener);
        categoryRice.setOnClickListener(categoryClickListener);
        categoryDesserts.setOnClickListener(categoryClickListener);
    }

    private void populateFoodList() {
        // Clear existing items
        foodListLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        // Sample food data - in real app, this would come from database/API
        List<FoodItem> foodItems = Arrays.asList(
                new FoodItem("Nasi Lemak", "Rice", 8.99, R.drawable.img_food1),
                new FoodItem("Chicken Fried Rice", "Rice", 10.99, R.drawable.ic_food),
                new FoodItem("Orange Juice", "Drinks", 4.99, R.drawable.ic_food),
                new FoodItem("Chocolate Cake", "Desserts", 6.99, R.drawable.ic_food),
                new FoodItem("Beef Noodles", "Noodles", 14.99, R.drawable.ic_food),
                new FoodItem("Vegetable Rice", "Rice", 9.99, R.drawable.ic_food),
                new FoodItem("Iced Coffee", "Drinks", 3.99, R.drawable.ic_food),
                new FoodItem("Cheesecake", "Desserts", 7.99, R.drawable.ic_food),
                new FoodItem("Seafood Pasta", "Noodles", 16.99, R.drawable.ic_food),
                new FoodItem("Egg Fried Rice", "Rice", 8.99, R.drawable.ic_food)
        );

        for (FoodItem item : foodItems) {
            View foodCard = inflater.inflate(R.layout.g_food_item_layout, foodListLayout, false);

            ImageView foodImage = foodCard.findViewById(R.id.food_image);
            TextView foodName = foodCard.findViewById(R.id.food_name);
            TextView foodCategory = foodCard.findViewById(R.id.food_category);
            TextView foodPrice = foodCard.findViewById(R.id.food_price);
            Button detailsButton = foodCard.findViewById(R.id.details_button);

            // Set food data
            foodImage.setImageResource(item.getImageRes());
            foodName.setText(item.getName());
            foodCategory.setText(item.getCategory());
            foodPrice.setText(String.format("$%.2f", item.getPrice()));

            // Set click listeners
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFoodDetails(item);
                }
            });

            foodCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFoodDetails(item);
                }
            });

            foodListLayout.addView(foodCard);
        }
    }

    private void filterFoodByCategory(String category) {
        // Clear current items
        foodListLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        List<FoodItem> allFoodItems = Arrays.asList(
                new FoodItem("Nasi Lemak", "Rice", 8.99, R.drawable.img_food1),
                new FoodItem("Chicken Fried Rice", "Rice", 10.99, R.drawable.ic_food),
                new FoodItem("Orange Juice", "Drinks", 4.99, R.drawable.ic_food),
                new FoodItem("Chocolate Cake", "Desserts", 6.99, R.drawable.ic_food),
                new FoodItem("Beef Noodles", "Noodles", 14.99, R.drawable.ic_food),
                new FoodItem("Vegetable Rice", "Rice", 9.99, R.drawable.ic_food),
                new FoodItem("Iced Coffee", "Drinks", 3.99, R.drawable.ic_food),
                new FoodItem("Cheesecake", "Desserts", 7.99, R.drawable.ic_food),
                new FoodItem("Seafood Pasta", "Noodles", 16.99, R.drawable.ic_food),
                new FoodItem("Egg Fried Rice", "Rice", 8.99, R.drawable.ic_food)
        );

        for (FoodItem item : allFoodItems) {
            // Show all items if "All" category is selected, otherwise filter by category
            if (category.equals("All") || item.getCategory().equals(category)) {
                View foodCard = inflater.inflate(R.layout.g_food_item_layout, foodListLayout, false);

                ImageView foodImage = foodCard.findViewById(R.id.food_image);
                TextView foodName = foodCard.findViewById(R.id.food_name);
                TextView foodCategory = foodCard.findViewById(R.id.food_category);
                TextView foodPrice = foodCard.findViewById(R.id.food_price);
                Button detailsButton = foodCard.findViewById(R.id.details_button);

                foodImage.setImageResource(item.getImageRes());
                foodName.setText(item.getName());
                foodCategory.setText(item.getCategory());
                foodPrice.setText(String.format("$%.2f", item.getPrice()));

                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFoodDetails(item);
                    }
                });

                foodCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFoodDetails(item);
                    }
                });

                foodListLayout.addView(foodCard);
            }
        }
    }

    private void showFoodDetails(FoodItem foodItem) {
        // Show food details - you can implement a dialog or new activity
        // For now, let's show a simple toast
        android.widget.Toast.makeText(this,
                "Details for: " + foodItem.getName() + "\nPrice: $" + foodItem.getPrice(),
                android.widget.Toast.LENGTH_SHORT).show();

        // You can replace this with:
        // Intent intent = new Intent(this, FoodDetailActivity.class);
        // intent.putExtra("food_item", foodItem);
        // startActivity(intent);
    }

    private void setupBottomNavigation() {
        Button reservationBtn = findViewById(R.id.reservation_btn);
        Button menuBtn = findViewById(R.id.menu_btn);
        Button profileBtn = findViewById(R.id.profile_btn);

        // Menu button - already on menu, so refresh
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMenu();
            }
        });

        // Reservation button - navigate to reservation
        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Menu.this, G_Reservation.class);
                startActivity(intent);
            }
        });

        // Profile button - navigate to profile
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G_Menu.this, G_Profile.class);
                startActivity(intent);
            }
        });
    }

    private void refreshMenu() {
        // Refresh the menu content
        populateFoodList();
        android.widget.Toast.makeText(this, "Menu refreshed", android.widget.Toast.LENGTH_SHORT).show();
    }

    // FoodItem inner class
    public static class FoodItem {
        private String name;
        private String category;
        private double price;
        private int imageRes;

        public FoodItem(String name, String category, double price, int imageRes) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.imageRes = imageRes;
        }

        // Getters
        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getImageRes() { return imageRes; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setCategory(String category) { this.category = category; }
        public void setPrice(double price) { this.price = price; }
        public void setImageRes(int imageRes) { this.imageRes = imageRes; }
    }
}