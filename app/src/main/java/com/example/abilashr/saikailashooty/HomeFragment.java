package com.example.abilashr.saikailashooty;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    TextView thoughtTitle, thoughtDetail;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        thoughtTitle=(TextView)rootView.findViewById(R.id.thoughtitle);
        thoughtDetail =(TextView)rootView.findViewById(R.id.thoughtdetail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("thought");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("title").getValue(String.class);
                String desc = dataSnapshot.child("data").getValue(String.class);
                thoughtTitle.setText(value);
                thoughtTitle.setPaintFlags(thoughtTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                thoughtDetail.setText(desc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
