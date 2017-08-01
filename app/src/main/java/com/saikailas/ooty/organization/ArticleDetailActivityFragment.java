package com.saikailas.ooty.organization;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleDetailActivityFragment extends Fragment {

    @BindView(R.id.articleDetailList)
    ListView articleDetailList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final List<String> parakeyList = new ArrayList<>();
        final Map<String, String> titleList = new HashMap<>();
        final Map<String, String> descList = new HashMap<>();
        final Map<String, Map<String, String>> imageRef = new HashMap<>();
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();


        parakeyList.add(getResources().getString(R.string.one));
        titleList.put(getResources().getString(R.string.one), getResources().getString(R.string.loading));
        descList.put(getResources().getString(R.string.one), getResources().getString(R.string.blank));


        View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ButterKnife.bind(this, rootView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(getResources().getString(R.string.article));
        final String titleReceived = getActivity().getIntent().getStringExtra(getResources().getString(R.string.title));

        final ArticleBaseAdapter articleBaseAdapter = new ArticleBaseAdapter(getActivity(), parakeyList, titleList, descList, imageRef);
        articleDetailList.setAdapter(articleBaseAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded() && getActivity() != null) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String title = snapshot.child(getResources().getString(R.string.title)).getValue(String.class);
                        for(DataSnapshot footprintChild : snapshot.getChildren()) {
                            if(footprintChild.getKey().toString().equals(getResources().getString(R.string.paragraph))) {
                                for(DataSnapshot paragraph :
                                        footprintChild.getChildren()) {
                                    String paratitle = paragraph.child(getResources().getString(R.string.paratitle)).getValue(String.class);
                                    String paradescription = paragraph.child(getResources().getString(R.string.paradetail)).getValue(String.class);
                                    parakeyList.add(paragraph.getKey());
                                    titleList.put(paragraph.getKey(), paratitle);
                                    descList.put(paragraph.getKey(), paradescription);
                                    for(DataSnapshot paragraphChild :
                                            paragraph.getChildren()) {
                                        if(paragraphChild.getKey().toString().equals(getResources().getString(R.string.image))) {
                                            final Map<String, String> imageData = new HashMap<String, String>();
                                            for(DataSnapshot images :
                                                    paragraphChild.getChildren()) {
                                                final String url = "gs://sai-kailas.appspot.com/article/" + snapshot.getKey() + "/" + footprintChild.getKey() + "/" + paragraph.getKey() + "/" + paragraphChild.getKey() + "/" + images.getKey();

                                                final String imageDescription = images.child(getResources().getString(R.string.description)).getValue().toString();
                                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(url);
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageData.put(uri.toString(), imageDescription);
                                                    }
                                                });
                                            }
                                            imageRef.put(paragraph.getKey(), imageData);
                                        }

                                    }
                                }
                                articleBaseAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().supportFinishAfterTransition();
        return true;
    }
}
