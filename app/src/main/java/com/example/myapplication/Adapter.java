package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    Context context;
    ArrayList<RecycleUnit> recycleUnits;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    public Adapter(Context context, ArrayList<RecycleUnit> recycleUnits) {
        this.context = context;
        this.recycleUnits = recycleUnits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.approval_registration_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the RecycleUnit object at the current position
        RecycleUnit recycleUnit = recycleUnits.get(position);
        holder.username.setText(recycleUnit.getUsername());
        holder.date.setText(recycleUnit.getDate());
        holder.materialType.setText(recycleUnit.getMaterialType());
        holder.quantity.setText(String.valueOf(recycleUnit.getQuantity()));

        // Set onClickListeners for the accept and decline buttons
        handleDeclineRecycleButton(holder, position);
        handleAcceptRecycleButton(holder, position);
    }

    private void handleDeclineRecycleButton(@NonNull ViewHolder holder, int position) {
        holder.decline_recycle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    RecycleUnit recycleUnit = recycleUnits.get(position);
                    String userId = recycleUnit.getUserId();
                    myRef.child("Recycle").child(userId).removeValue();
                }
            }
        });
    }

    private void handleAcceptRecycleButton(@NonNull ViewHolder holder, int position) {
        holder.accept_recycle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    // Get the RecycleUnit object at the current position
                    RecycleUnit recycleUnit = recycleUnits.get(position);
                    String typeOfMaterial = recycleUnit.getMaterialType();
                    String date = recycleUnit.getDate();
                    int quantity = recycleUnit.getQuantity();
                    int pointsToAdd = quantity * 10;
                    String userId = recycleUnit.getUserId();

                    // Remove the RecycleUnit from the database
                    myRef.child("Recycle").child(userId).removeValue();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Update the points field
                            int currentPoints = snapshot.child("points").getValue(Integer.class);
                            int newPoints = currentPoints + pointsToAdd;
                            userRef.child("points").setValue(newPoints);

                            // Update the recycled material fields
                            updateRecycledMaterial(userRef, snapshot, typeOfMaterial, quantity);

                            // Update the recycleDates field
                            List<String> recycleDates = (List<String>) snapshot.child("recycleDates").getValue();
                            if (recycleDates == null) {
                                recycleDates = new ArrayList<>();
                            }
                            recycleDates.add(date);
                            userRef.child("recycleDates").setValue(recycleDates);

                            // Calculate the streak and update the streak field
                            int streak = calculateStreak(recycleDates);
                            userRef.child("streak").setValue(streak);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
    }

    private void updateRecycledMaterial(DatabaseReference userRef, DataSnapshot snapshot, String typeOfMaterial, int quantity) {
        if(typeOfMaterial.equals("Plastic")){
            int currentPlastic = snapshot.child("plastic_recycled").getValue(Integer.class);
            int newPlastic = currentPlastic + quantity;
            userRef.child("plastic_recycled").setValue(newPlastic);
        }
        else if(typeOfMaterial.equals("Paper")){
            int currentPaper = snapshot.child("paper_recycled").getValue(Integer.class);
            int newPaper = currentPaper + quantity;
            userRef.child("paper_recycled").setValue(newPaper);
        }
        else if(typeOfMaterial.equals("Glass")){
            int currentGlass = snapshot.child("glass_recycled").getValue(Integer.class);
            int newGlass = currentGlass + quantity;
            userRef.child("glass_recycled").setValue(newGlass);
        }
    }

    public int calculateStreak(List<String> recycleDates) {
        Collections.sort(recycleDates);
        int streak = 1;
        int maxStreak = 1;

        for (int i = 1; i < recycleDates.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date1 = sdf.parse(recycleDates.get(i - 1));
                Date date2 = sdf.parse(recycleDates.get(i));
                long diff = date2.getTime() - date1.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                if (diffDays == 0) {
                    continue;
                }
                if (diffDays == 1) {
                    streak++;
                    if (streak > maxStreak) {
                        maxStreak = streak;
                    }
                } else if (diffDays > 1) {
                    streak = 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return maxStreak;
    }

    @Override
    public int getItemCount() {
        return recycleUnits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, date, materialType, quantity;
        Button accept_recycle_btn, decline_recycle_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.recycler_username);
            date = itemView.findViewById(R.id.recycler_date);
            materialType = itemView.findViewById(R.id.recycler_material_type);
            quantity = itemView.findViewById(R.id.recycler_quantity);
            accept_recycle_btn = itemView.findViewById(R.id.accept_recycle);
            decline_recycle_btn = itemView.findViewById(R.id.decline_recycle);
        }
    }
}