package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMainActivity extends AppCompatActivity {

    Button logoutButton;
    ProgressBar progressBar;
    TextView usernameField;
    TextView pointsField;
    TextView tokenField;
    TextView pointsMonlthyField;
    TextView plasticMonthlyField;
    TextView streaksMonthlyField;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Initialize UI elements
        initializeUI();

        // Setup Firebase user
        setupFirebaseUser();

        // Setup logout button
        setupLogoutButton();

        // Setup recycle button
        setupRecycleButton();
    }

    private void initializeUI() {

        usernameField= findViewById(R.id.user_name);
        pointsField= findViewById(R.id.points);
        tokenField= findViewById(R.id.tokens);
        pointsMonlthyField= findViewById(R.id.points_monthly);
        plasticMonthlyField= findViewById(R.id.plastic_monthly);
        streaksMonthlyField= findViewById(R.id.streaks_monthly);
        progressBar = findViewById(R.id.progress);
    }

    private void setupFirebaseUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }

        // Set the attributes of the user

        setAttributes("username",usernameField);
        setAttributes("points",pointsField);
        setAttributes("plastic_recycled",plasticMonthlyField);
        setAttributes("streak",streaksMonthlyField);
    }

    private void setupLogoutButton() {
        logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupRecycleButton() {
        Button recycleButton = findViewById(R.id.btn_recycle);
        recycleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecycleFormActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setAttributes(String attribute, TextView attributeField){
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (attribute.equals("points")) {
                        Long points = dataSnapshot.child(attribute).getValue(Long.class);
                        attributeField.setText(String.valueOf(points));
                        tokenField.setText("Tokens "+String.valueOf(points/100));
                        pointsMonlthyField.setText(String.valueOf(points));

                        int progress = (int) (points % 100);
                        progressBar.setProgress(progress);

                    } else if (attribute.equals("streak")) {
                        Long streak = dataSnapshot.child(attribute).getValue(Long.class);
                        attributeField.setText(String.valueOf(streak));
                    } else if(attribute.equals("plastic_recycled")) {
                        Long plastic_recycled = dataSnapshot.child(attribute).getValue(Long.class);
                        attributeField.setText(String.valueOf(plastic_recycled)+" kg");
                    } else {
                        String attributeText = dataSnapshot.child(attribute).getValue(String.class);
                        attributeField.setText(attributeText);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}