package com.saikailas.ooty.organization;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleBaseAdapter extends BaseAdapter {
    Context context;
    private final List<String> paraKeyList;
    private final List<String> imageList;
    private final List<String> descriptionList;
    private final Map<String, String> titleList;
    private final Map<String, String> descList;
    private final Map<String, Map<String, String>> imageRef;

    public ArticleBaseAdapter(Context context, List<String> paraKeyList, Map<String, String> titleList, Map<String, String> descList, Map<String, Map<String, String>> imageRef) {

        this.context = context;
        this.paraKeyList = paraKeyList;
        this.titleList = titleList;
        this.descList = descList;
        this.imageRef = imageRef;
        this.imageList = new ArrayList<>();
        this.descriptionList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return paraKeyList.size();
    }

    @Override
    public Object getItem(int i) {
        return paraKeyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return paraKeyList.indexOf(getItem(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        imageList.clear();
        descriptionList.clear();
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.article_detail, null);
        TextView title = (TextView) convertView.findViewById(R.id.paraTitle);
        TextView description = (TextView) convertView.findViewById(R.id.paraDetail);
        String para = paraKeyList.get(position);
        if(!titleList.get(para).equals("")) {
            title.setText(titleList.get(para));
        }
        description.setText(Html.fromHtml(descList.get(para)));

        if(imageRef.get(para) != null) {


            LinearLayout myRoot = (LinearLayout) convertView.findViewById(R.id.articleDetail);
            LinearLayout a = new LinearLayout(context);

            a.setOrientation(LinearLayout.VERTICAL);


            for(String key : imageRef.get(para).keySet()) {
                ImageView imageView = new ImageView(context);
                Glide.with(context)
                        .load(key)
                        .into(imageView);
                a.addView(imageView);
            }
            myRoot.addView(a);
        }


        return convertView;
    }
}
