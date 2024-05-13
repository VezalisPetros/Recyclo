package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecycleFormActivity extends AppCompatActivity {

    TextView dateText;
    Button dateButton;
    AutoCompleteTextView autoCompleteText;
    ArrayAdapter<String> adapterItems;
    Button backtohome;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_form);

        // Initialize UI components
        initializeUI();

        // Setup submit button
        setupSubmitButton();

        // Setup back to home button
        setupBackToHomeButton();

        // Setup date picker button
        setupDatePickerButton();
    }

    private void initializeUI() {
        dateText = findViewById(R.id.selected_date);
        dateButton = findViewById(R.id.btn_select_date);
        autoCompleteText = findViewById(R.id.material_type);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, getResources().getStringArray(R.array.material_array));
        autoCompleteText.setAdapter(adapterItems);
    }

    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.btn_submit_recycle_form);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = ((TextInputEditText) findViewById(R.id.selected_date)).getText().toString();
                String materialType = ((AutoCompleteTextView) findViewById(R.id.material_type)).getText().toString();
                String quantity = ((TextInputEditText) findViewById(R.id.quantity)).getText().toString();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String username = dataSnapshot.child("username").getValue(String.class);
                            RecycleUnit recycleUnit = new RecycleUnit(date, materialType, Integer.parseInt(quantity), username,userId);
                            myRef.child("Recycle").child(currentUser.getUid()).setValue(recycleUnit);
                            Toast.makeText(RecycleFormActivity.this, "Recycle Unit Added", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
    }

    private void setupBackToHomeButton() {
        backtohome = findViewById(R.id.btn_back_to_home);
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupDatePickerButton() {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateText.setText(String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth));
            }
        }, 2024, 5, 15);
        dialog.show();
    }
}