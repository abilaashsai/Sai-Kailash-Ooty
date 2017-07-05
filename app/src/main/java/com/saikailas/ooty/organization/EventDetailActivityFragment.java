package com.saikailas.ooty.organization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saikailas.ooty.organization.data.DataContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailActivityFragment extends Fragment {

    @BindView(R.id.eventTitleDate)
    TextView eventTitleDate;

    @BindView(R.id.eventTitleName)
    TextView eventTitleName;

    @BindView(R.id.eventDetail)
    TextView eventDetail;

    @BindView(R.id.imageListView)
    ListView listView;

    public EventDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, rootView);

        final String dateString = getActivity().getIntent().getStringExtra(getResources().getString(R.string.date));
        final String name = getActivity().getIntent().getStringExtra(getResources().getString(R.string.title));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setContentDescription(getResources().getString(R.string.share_button));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, dateString + " - " + name);
                startActivity(Intent.createChooser(sharingIntent, getActivity().getResources().getString(R.string.ShareVia)));
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String events = getResources().getString(R.string.events);
        String upcoming = getResources().getString(R.string.upcoming);
        DatabaseReference databaseReference = database.getReference(events + "/" + upcoming + "/" + getReferenceDate(dateString));
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        eventTitleDate.setText(getDateDisplayFormat(dateString));
        eventTitleName.setText(name);
        eventDetail.setText(getResources().getString(R.string.loading));
        final ArrayList arrayList = new ArrayList();
        final CustomImageAdapter customImageAdapter = new CustomImageAdapter(getContext(), arrayList);

        listView.setAdapter(customImageAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded() && getActivity() != null) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getKey().equals(getResources().getString(R.string.date)) || snapshot.getKey().equals(getResources().getString(R.string.message)) || snapshot.getKey().equals(getResources().getString(R.string.type))) {
                        } else if(snapshot.getKey().equals(getResources().getString(R.string.detail))) {
                            eventDetail.setText(dataSnapshot.child(getResources().getString(R.string.detail)).getValue(String.class));
                        } else {
                            for(DataSnapshot image : snapshot.getChildren()) {
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://sai-kailas.appspot.com/events/upcoming/" + getReferenceDate(dateString) + "/image/" + image.getKey());
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        arrayList.add(uri.toString());
                                        customImageAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
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

    private String getReferenceDate(String dateString) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "yyyy/M/d";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String dateOutput = null;

        try {
            date = inputFormat.parse(dateString);
            dateOutput = outputFormat.format(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    private String getDateDisplayFormat(String dateString) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String dateOutput = null;

        try {
            date = inputFormat.parse(dateString);
            dateOutput = outputFormat.format(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().supportFinishAfterTransition();
        return true;
    }
}
