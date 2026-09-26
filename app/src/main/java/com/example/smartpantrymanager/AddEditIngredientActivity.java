package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private EditText editExpiryDate;

    private DatabaseHelper databaseHelper;

    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        TextView textFormTitle = findViewById(R.id.textFormTitle);
        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiryDate = findViewById(R.id.editExpiryDate);

        Button buttonSaveIngredient =
                findViewById(R.id.buttonSaveIngredient);

        Button buttonCancel =
                findViewById(R.id.buttonCancel);

        databaseHelper = new DatabaseHelper(this);

        ingredientId =
                getIntent().getIntExtra("INGREDIENT_ID", -1);

        if (ingredientId != -1) {
            textFormTitle.setText("Edit Ingredient");
            buttonSaveIngredient.setText("Update Ingredient");
            loadIngredient();
        }

        editExpiryDate.setOnClickListener(
                v -> showDatePicker()
        );

        buttonSaveIngredient.setOnClickListener(
                v -> saveIngredient()
        );

        buttonCancel.setOnClickListener(
                v -> finish()
        );
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {

                            String selectedDate =
                                    String.format(
                                            Locale.getDefault(),
                                            "%04d-%02d-%02d",
                                            year,
                                            month + 1,
                                            dayOfMonth
                                    );

                            editExpiryDate.setText(selectedDate);
                            editExpiryDate.setError(null);
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

        datePickerDialog.show();
    }

    private void loadIngredient() {

        Ingredient ingredient =
                databaseHelper.getIngredient(ingredientId);

        if (ingredient == null) {

            Toast.makeText(
                    this,
                    "Ingredient could not be found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        editIngredientName.setText(
                ingredient.getName()
        );

        editQuantity.setText(
                String.valueOf(
                        ingredient.getQuantity()
                )
        );

        editUnit.setText(
                ingredient.getUnit()
        );

        editExpiryDate.setText(
                ingredient.getExpiryDate()
        );
    }

    private void saveIngredient() {

        String name =
                editIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                editQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                editUnit
                        .getText()
                        .toString()
                        .trim();

        String expiryDate =
                editExpiryDate
                        .getText()
                        .toString()
                        .trim();

        // Validate ingredient name
        if (name.isEmpty()) {

            editIngredientName.setError(
                    "Enter an ingredient name."
            );

            editIngredientName.requestFocus();
            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            editQuantity.setError(
                    "Enter a quantity."
            );

            editQuantity.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException exception) {

            editQuantity.setError(
                    "Enter a valid quantity."
            );

            editQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {

            editQuantity.setError(
                    "Quantity must be greater than zero."
            );

            editQuantity.requestFocus();
            return;
        }

        // Validate measurement unit
        if (unit.isEmpty()) {

            editUnit.setError(
                    "Enter a measurement unit."
            );

            editUnit.requestFocus();
            return;
        }

        // Validate expiry date
        if (expiryDate.isEmpty()) {

            editExpiryDate.setError(
                    "Select an expiry date."
            );

            editExpiryDate.requestFocus();
            return;
        }

        if (ingredientId == -1) {

            Ingredient ingredient =
                    new Ingredient(
                            name,
                            quantity,
                            unit,
                            expiryDate
                    );

            long newId =
                    databaseHelper.addIngredient(
                            ingredient
                    );

            if (newId != -1) {

                Toast.makeText(
                        this,
                        "Ingredient added.",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Ingredient could not be saved.",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            Ingredient ingredient =
                    new Ingredient(
                            ingredientId,
                            name,
                            quantity,
                            unit,
                            expiryDate
                    );

            int updatedRows =
                    databaseHelper.updateIngredient(
                            ingredient
                    );

            if (updatedRows > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated.",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Ingredient could not be updated.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}