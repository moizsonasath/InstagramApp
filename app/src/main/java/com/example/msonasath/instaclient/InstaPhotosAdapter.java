package com.example.msonasath.instaclient;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by m.sonasath on 12/1/2015.
 */
public class InstaPhotosAdapter extends ArrayAdapter {
    String caption;
    String likes;
    //what data do we need from the activity
    public InstaPhotosAdapter(Context context, List<InstaPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //use the template to dispaly each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data item for this position
        InstaPhoto photo = (InstaPhoto) getItem(position);
        //check if we are using a recycled view, if not we need to inflate
        if (convertView == null){
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //lookup the view for populating data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        // insert model data into each of the view items
        caption = "<font color=\"#206199\"><b>" + photo.username + ": " + "</b></font>" + "<font color=\"#000000\">" + photo.caption + "</font>";
        likes = "<font color=\"#206199\">" + Integer.toString(photo.likesCount) + " Likes" + "</font>";
        tvCaption.setText(Html.fromHtml(caption));
        tvLikes.setText(Html.fromHtml(likes));
        //clear out imageview
        ivPhoto.setImageResource(0);
        //insert image using picaso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        tvFullName.setText(photo.userFullName);
        CharSequence relativeTimespan = DateUtils.getRelativeTimeSpanString(
                photo.createdTime * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        String[] timespanParts = relativeTimespan.toString().split(" ");
        String formattedTimespan = timespanParts[0] + timespanParts[1].charAt(0);
        tvTime.setText(formattedTimespan);
        Picasso.with(getContext()).load(photo.userImageUrl).into(ivProfile);

        //return the created item as a view
        return convertView;
    }
}
