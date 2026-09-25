package com.example.smartpantrymanager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ingredientContainer;
    private TextView textEmptyPantry;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingredientContainer =
                findViewById(R.id.ingredientContainer);

        textEmptyPantry =
                findViewById(R.id.textEmptyPantry);

        Button buttonAddIngredient =
                findViewById(R.id.buttonAddIngredient);

        databaseHelper = new DatabaseHelper(this);

        buttonAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayIngredients();
    }

    private void displayIngredients() {

        ingredientContainer.removeAllViews();

        List<Ingredient> ingredients =
                databaseHelper.getAllIngredients();

        if (ingredients.isEmpty()) {
            textEmptyPantry.setVisibility(View.VISIBLE);
            return;
        }

        textEmptyPantry.setVisibility(View.GONE);

        for (Ingredient ingredient : ingredients) {
            addIngredientView(ingredient);
        }
    }

    private void addIngredientView(Ingredient ingredient) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(24, 22, 24, 22);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 20);

        card.setLayoutParams(cardParams);
        card.setBackgroundColor(0xFFFFFFFF);

        TextView nameText = new TextView(this);

        nameText.setText(ingredient.getName());
        nameText.setTextSize(20);
        nameText.setTypeface(null, Typeface.BOLD);
        nameText.setTextColor(0xFF24352A);

        card.addView(nameText);

        TextView quantityText = new TextView(this);

        quantityText.setText(
                formatQuantity(ingredient.getQuantity())
                        + " "
                        + ingredient.getUnit()
        );

        quantityText.setTextSize(16);
        quantityText.setTextColor(0xFF58635B);
        quantityText.setPadding(0, 8, 0, 4);

        card.addView(quantityText);

        TextView expiryText = new TextView(this);

        if (ingredient.getExpiryDate() == null
                || ingredient.getExpiryDate().trim().isEmpty()) {

            expiryText.setText("No expiry date");

        } else {

            expiryText.setText(
                    "Expires: " + ingredient.getExpiryDate()
            );
        }

        expiryText.setTextSize(14);
        expiryText.setTextColor(0xFF747C76);

        card.addView(expiryText);

        LinearLayout actions = new LinearLayout(this);

        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.END);

        Button editButton = new Button(this);

        editButton.setText("Edit");

        Button deleteButton = new Button(this);

        deleteButton.setText("Delete");

        actions.addView(editButton);
        actions.addView(deleteButton);

        card.addView(actions);

        editButton.setOnClickListener(
                v -> editIngredient(ingredient)
        );

        deleteButton.setOnClickListener(
                v -> confirmDelete(ingredient)
        );

        ingredientContainer.addView(card);
    }

    private String formatQuantity(double quantity) {

        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    private void editIngredient(Ingredient ingredient) {

        Intent intent = new Intent(
                MainActivity.this,
                AddEditIngredientActivity.class
        );

        intent.putExtra(
                "INGREDIENT_ID",
                ingredient.getId()
        );

        startActivity(intent);
    }

    private void confirmDelete(Ingredient ingredient) {

        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage(
                        "Remove "
                                + ingredient.getName()
                                + " from your pantry?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) ->
                                deleteIngredient(ingredient)
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteIngredient(Ingredient ingredient) {

        int deletedRows =
                databaseHelper.deleteIngredient(
                        ingredient.getId()
                );

        if (deletedRows > 0) {

            Toast.makeText(
                    this,
                    "Ingredient removed.",
                    Toast.LENGTH_SHORT
            ).show();

            displayIngredients();

        } else {

            Toast.makeText(
                    this,
                    "Ingredient could not be removed.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}