package com.example.abilashr.saikailashooty;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArticleFragment extends Fragment {
    public ArticleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        final ArrayList arrayList = new ArrayList();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(getResources().getString(R.string.article));
        final CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getContext(), arrayList);
        ListView listView = (ListView) rootView.findViewById(R.id.articleList);
        listView.setAdapter(customBaseAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded() && getActivity() != null) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String title = snapshot.child(getResources().getString(R.string.title)).getValue(String.class);
                        arrayList.add(title);
                    }
                    customBaseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
