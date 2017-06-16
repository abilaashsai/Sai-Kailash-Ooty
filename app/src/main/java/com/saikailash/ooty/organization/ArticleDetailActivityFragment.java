package com.saikailash.ooty.organization;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleDetailActivityFragment extends Fragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.detail)
    TextView detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ButterKnife.bind(this, rootView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(getResources().getString(R.string.article));
        final String titleReceived = getActivity().getIntent().getStringExtra(getResources().getString(R.string.title));
        title.setText(titleReceived);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded() && getActivity() != null) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String title = snapshot.child(getResources().getString(R.string.title)).getValue(String.class);
                        if(title.equals(titleReceived)) {
                            detail.setText(snapshot.child(getResources().getString(R.string.detail)).getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().supportFinishAfterTransition();
        return true;
    }
}
