package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private LinearLayout recipeContainer;
    private DatabaseHelper databaseHelper;
    private final List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recipeContainer = findViewById(R.id.recipeContainer);
        databaseHelper = new DatabaseHelper(this);

        createRecipeCollection();

        findViewById(R.id.backToPantryButton).setOnClickListener(v -> finish());

        showSuggestedRecipes();
    }

    private void createRecipeCollection() {

        recipes.add(new Recipe(
                "Tomato Rice",
                "rice,tomato",
                "Cook the rice until tender. Chop the tomato and mix it through the cooked rice. Season and serve."
        ));

        recipes.add(new Recipe(
                "Chicken and Rice",
                "chicken breast,rice",
                "Cook the rice. Cut the chicken into pieces and cook thoroughly. Serve the chicken over the rice."
        ));

        recipes.add(new Recipe(
                "Chicken Tomato Rice",
                "chicken breast,rice,tomato",
                "Cook the rice. Cook the chicken thoroughly. Chop the tomato and combine all ingredients before serving."
        ));

        recipes.add(new Recipe(
                "Scrambled Eggs",
                "egg",
                "Beat the eggs and cook them in a pan while stirring gently until set."
        ));

        recipes.add(new Recipe(
                "Cheese Omelette",
                "egg,cheese",
                "Beat the eggs, pour into a pan and cook gently. Add cheese, fold the omelette and serve."
        ));

        recipes.add(new Recipe(
                "Tomato Omelette",
                "egg,tomato",
                "Beat the eggs. Add chopped tomato and cook in a pan until the eggs are completely set."
        ));

        recipes.add(new Recipe(
                "Cheese Sandwich",
                "bread,cheese",
                "Place cheese between slices of bread. Serve cold or toast until the cheese melts."
        ));

        recipes.add(new Recipe(
                "Tomato Sandwich",
                "bread,tomato",
                "Slice the tomato and place it between slices of bread. Season if desired and serve."
        ));

        recipes.add(new Recipe(
                "Chicken Sandwich",
                "bread,chicken breast",
                "Cook the chicken thoroughly, slice it and place it between slices of bread."
        ));

        recipes.add(new Recipe(
                "Chicken Tomato Sandwich",
                "bread,chicken breast,tomato",
                "Cook and slice the chicken. Add chicken and sliced tomato to the bread and serve."
        ));

        recipes.add(new Recipe(
                "Mashed Potato",
                "potato,milk",
                "Boil the potato until soft. Mash it and gradually mix in the milk."
        ));

        recipes.add(new Recipe(
                "Cheesy Potato",
                "potato,cheese",
                "Cook the potato until tender. Add cheese while hot and allow it to melt."
        ));

        recipes.add(new Recipe(
                "Tomato Pasta",
                "pasta,tomato",
                "Cook the pasta. Chop and cook the tomato, then combine it with the pasta."
        ));

        recipes.add(new Recipe(
                "Cheesy Pasta",
                "pasta,cheese",
                "Cook the pasta until tender. Drain it and stir in the cheese while hot."
        ));

        recipes.add(new Recipe(
                "Chicken Pasta",
                "pasta,chicken breast",
                "Cook the pasta. Cook the chicken thoroughly, slice it and combine it with the pasta."
        ));

        recipes.add(new Recipe(
                "Tuna Sandwich",
                "bread,tuna",
                "Drain the tuna and place it between slices of bread."
        ));

        recipes.add(new Recipe(
                "Tuna Pasta",
                "pasta,tuna",
                "Cook and drain the pasta. Add the drained tuna and combine well."
        ));

        recipes.add(new Recipe(
                "Egg Fried Rice",
                "rice,egg",
                "Cook the rice. Cook the beaten egg in a pan, then add the rice and combine."
        ));

        recipes.add(new Recipe(
                "Chicken Potato Bowl",
                "chicken breast,potato",
                "Cook the potato until tender. Cook the chicken thoroughly, slice it and serve together."
        ));

        recipes.add(new Recipe(
                "Tomato and Cheese Toast",
                "bread,tomato,cheese",
                "Place sliced tomato and cheese on bread and toast until the cheese melts."
        ));
    }

    private void showSuggestedRecipes() {

        recipeContainer.removeAllViews();

        List<Ingredient> pantry = databaseHelper.getAllIngredients();

        int matches = 0;

        for (Recipe recipe : recipes) {

            if (canMakeRecipe(recipe, pantry)) {
                addRecipeCard(recipe);
                matches++;
            }
        }

        if (matches == 0) {

            TextView emptyView = new TextView(this);
            emptyView.setText(
                    "No recipes match your pantry yet.\n\n" +
                            "Add more ingredients to unlock recipe suggestions."
            );
            emptyView.setTextSize(17);
            emptyView.setPadding(20, 50, 20, 20);

            recipeContainer.addView(emptyView);
        }
    }

    private boolean canMakeRecipe(Recipe recipe, List<Ingredient> pantry) {

        List<String> requiredIngredients =
                Arrays.asList(recipe.getIngredients().split(","));

        for (String required : requiredIngredients) {

            boolean found = false;
            String requiredNormalised = normaliseIngredient(required);

            for (Ingredient pantryIngredient : pantry) {

                String pantryNormalised =
                        normaliseIngredient(pantryIngredient.getName());

                if (pantryNormalised.equals(requiredNormalised)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    private String normaliseIngredient(String ingredient) {

        String value = ingredient
                .trim()
                .toLowerCase(Locale.ROOT);

        if (value.endsWith("oes")) {
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("s") && value.length() > 3) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }

    private void addRecipeCard(Recipe recipe) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(24, 22, 24, 22);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);

        card.setBackgroundColor(0xFFFFFFFF);
        card.setElevation(4);

        TextView title = new TextView(this);
        title.setText(recipe.getName());
        title.setTextSize(19);
        title.setTextColor(0xFF1B1B1B);
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView ingredients = new TextView(this);
        ingredients.setText(
                "Uses: " + recipe.getIngredients().replace(",", ", ")
        );
        ingredients.setPadding(0, 8, 0, 8);

        TextView open = new TextView(this);
        open.setText("View recipe →");
        open.setTextSize(15);
        open.setPadding(0, 12, 0, 4);

        card.addView(title);
        card.addView(ingredients);
        card.addView(open);

        card.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SuggestedRecipesActivity.this,
                            RecipeDetailActivity.class
                    );

            intent.putExtra("recipeName", recipe.getName());
            intent.putExtra("recipeIngredients", recipe.getIngredients());
            intent.putExtra("recipeInstructions", recipe.getInstructions());

            startActivity(intent);
        });

        recipeContainer.addView(card);
    }
}