package com.saikailas.ooty.organization;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomImageAdapter extends BaseAdapter {
    Context context;
    List<String> imageUrl;

    public CustomImageAdapter(Context context, List<String> imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageUrl.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.event_image, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.eventImage);
        Glide.with(context)
                .load(imageUrl.get(position))
                .into(imageView);

        return convertView;
    }
}
