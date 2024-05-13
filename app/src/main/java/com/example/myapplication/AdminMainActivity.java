package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AdminMainActivity extends AppCompatActivity {

    // Declare UI elements and Firebase references
    RecyclerView recyclerView;

    TextView emptyView;
    ConstraintLayout approval_registration_layout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Adapter adapter;
    ArrayList<RecycleUnit> recycleUnits;
    Button highestPoints, highestPlastic, highestStreak;
    TextView most_points_gold, most_points_silver, most_points_bronze;
    TextView most_plastic_gold, most_plastic_silver, most_plastic_bronze;
    TextView most_streak_gold, most_streak_silver, most_streak_bronze;
    TextView username_points_gold, username_points_silver, username_points_bronze;
    TextView username_plastic_gold, username_plastic_silver, username_plastic_bronze;
    TextView username_streak_gold, username_streak_silver, username_streak_bronze;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Initialize UI elements
        initializeUI();

        // Set onClickListeners for buttons
        setOnClickListeners();

        // Connect to the Recycle database and display the data in the Recycle database in the RecyclerView
        displayRecycleData();

        // Set onClickListener for logout button
        setLogoutButton();

        // Find the top 3 users with the highest points and plastic recycled
        findTopUsers("points", most_points_gold, username_points_gold, most_points_silver, username_points_silver, most_points_bronze, username_points_bronze);
        findTopUsers("plastic_recycled", most_plastic_gold, username_plastic_gold, most_plastic_silver, username_plastic_silver, most_plastic_bronze, username_plastic_bronze);
        findTopUsers("streak", most_streak_gold, username_streak_gold, most_streak_silver, username_streak_silver, most_streak_bronze, username_streak_bronze);

    }

    private void initializeUI() {
        // Initialize the UI elements
        emptyView = findViewById(R.id.empty_view);
        approval_registration_layout = findViewById(R.id.approval_registration_layout);

        // Initialize the TextViews for the buttons to display the top users with the highest points, plastic recycled, and streak
        highestPoints = findViewById(R.id.highest_points);
        highestPlastic = findViewById(R.id.highest_plastic);
        highestStreak = findViewById(R.id.highest_streak);

        // Initialize the TextViews for the top 3 users with the highest points, plastic recycled, and streak
        most_points_gold = findViewById(R.id.most_points_gold);
        most_points_silver = findViewById(R.id.most_points_silver);
        most_points_bronze = findViewById(R.id.most_points_bronze);
        most_plastic_gold = findViewById(R.id.most_plastic_gold);
        most_plastic_silver = findViewById(R.id.most_plastic_silver);
        most_plastic_bronze = findViewById(R.id.most_plastic_bronze);
        most_streak_gold = findViewById(R.id.most_streak_gold);
        most_streak_silver = findViewById(R.id.most_streak_silver);
        most_streak_bronze = findViewById(R.id.most_streak_bronze);
        username_points_gold = findViewById(R.id.username_points_gold);
        username_points_silver = findViewById(R.id.username_points_silver);
        username_points_bronze = findViewById(R.id.username_points_bronze);
        username_plastic_gold = findViewById(R.id.username_plastic_gold);
        username_plastic_silver = findViewById(R.id.username_plastic_silver);
        username_plastic_bronze = findViewById(R.id.username_plastic_bronze);
        username_streak_gold = findViewById(R.id.username_streak_gold);
        username_streak_silver = findViewById(R.id.username_streak_silver);
        username_streak_bronze = findViewById(R.id.username_streak_bronze);
    }

    private void setOnClickListeners() {
        // Set onClickListeners for the buttons to display the top users with the highest points, plastic recycled, and streak
        highestPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.top_points).setVisibility(View.VISIBLE);
                findViewById(R.id.top_plastic).setVisibility(View.GONE);
                findViewById(R.id.top_streak).setVisibility(View.GONE);
            }
        });

        highestPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.top_points).setVisibility(View.GONE);
                findViewById(R.id.top_plastic).setVisibility(View.VISIBLE);
                findViewById(R.id.top_streak).setVisibility(View.GONE);
            }
        });

        highestStreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.top_points).setVisibility(View.GONE);
                findViewById(R.id.top_plastic).setVisibility(View.GONE);
                findViewById(R.id.top_streak).setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayRecycleData() {
        // Connect to the Recycle database and display the data in the Recycle database in the RecyclerView
        recyclerView = findViewById(R.id.approval_registration_list);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recycle");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recycleUnits=new ArrayList<>();
        adapter = new Adapter(this,recycleUnits);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                recycleUnits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RecycleUnit recycleUnit = dataSnapshot.getValue(RecycleUnit.class);
                    recycleUnits.add(recycleUnit);
                }
                if (recycleUnits.isEmpty()) {
                    // If there are no items to show, make the empty view visible
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    approval_registration_layout.setVisibility(View.GONE);
                } else {
                    // If there are items to show, make the RecyclerView visible
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    approval_registration_layout.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {

            }
        });
    }

    private void setLogoutButton() {
        // Set onClickListener for logout button
        Button logoutButton = findViewById(R.id.btn_logout);
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

    private void findTopUsers(String field, TextView goldValue, TextView goldUser, TextView silverValue, TextView silverUser, TextView bronzeValue, TextView bronzeUser) {
        // Find the top 3 users with the highest points, plastic recycled, and streak
        Query query = databaseReference.orderByChild(field).limitToLast(3);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Pair<String, Integer>> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    int value = snapshot.child(field).getValue(Integer.class);
                    users.add(new Pair<>(username, value));
                }

                Collections.sort(users, (a, b) -> b.second - a.second);

                if (users.size() > 0) {
                    goldValue.setText(String.valueOf(users.get(0).second));
                    goldUser.setText(users.get(0).first);
                }
                if (users.size() > 1) {
                    silverValue.setText(String.valueOf(users.get(1).second));
                    silverUser.setText(users.get(1).first);
                }
                if (users.size() > 2) {
                    bronzeValue.setText(String.valueOf(users.get(2).second));
                    bronzeUser.setText(users.get(2).first);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}