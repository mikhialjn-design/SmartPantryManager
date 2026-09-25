package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView nameText =
                findViewById(R.id.recipeNameText);

        TextView ingredientsText =
                findViewById(R.id.recipeIngredientsText);

        TextView instructionsText =
                findViewById(R.id.recipeInstructionsText);

        String name =
                getIntent().getStringExtra("recipeName");

        String ingredients =
                getIntent().getStringExtra("recipeIngredients");

        String instructions =
                getIntent().getStringExtra("recipeInstructions");

        nameText.setText(name);

        if (ingredients != null) {
            ingredientsText.setText(
                    "• " + ingredients.replace(",", "\n• ")
            );
        }

        instructionsText.setText(instructions);

        findViewById(R.id.backButton)
                .setOnClickListener(v -> finish());
    }
}