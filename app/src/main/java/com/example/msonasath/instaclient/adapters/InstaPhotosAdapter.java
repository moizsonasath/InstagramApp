package com.example.msonasath.instaclient.adapters;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.msonasath.instaclient.models.InstaPhoto;
import com.example.msonasath.instaclient.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by m.sonasath on 12/1/2015.
 */
public class InstaPhotosAdapter extends ArrayAdapter {
    String caption;
    String likes;

    private static class ViewHolder {
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvLikes;
        ImageView ivProfile;
        TextView tvFullName;
        TextView tvTime;
    }

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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

            //lookup the view for populating data (image, caption)
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
            viewHolder.tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // insert model data into each of the view items
        caption = "<font color=\"#206199\"><b>" + photo.username.trim() + ": " + "</b></font>" + "<font color=\"#000000\">" + photo.caption.trim() + "</font>";
        likes = "<font color=\"#206199\">" + Integer.toString(photo.likesCount) + " Likes" + "</font>";
        viewHolder.tvCaption.setText(Html.fromHtml(caption));
        viewHolder.tvLikes.setText(Html.fromHtml(likes));

        //clear out imageview
        viewHolder.ivPhoto.setImageResource(0);
        //insert image using picaso
        Picasso.with(getContext()).load(photo.imageUrl).fit().centerInside().placeholder(R.drawable.google_photos).error(R.drawable.error).into(viewHolder.ivPhoto);

        viewHolder.tvFullName.setText(photo.userFullName.trim());
        CharSequence relativeTimespan = DateUtils.getRelativeTimeSpanString(
                photo.createdTime * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        String[] timespanParts = relativeTimespan.toString().split(" ");
        String formattedTimespan = timespanParts[0] + timespanParts[1].charAt(0);
        viewHolder.tvTime.setText(formattedTimespan);

        viewHolder.ivProfile.setImageResource(0);
        Picasso.with(getContext()).load(photo.userImageUrl).fit().centerInside().placeholder(R.drawable.google_photos).error(R.drawable.error).into(viewHolder.ivProfile);

        //return the created item as a view
        return convertView;
    }
}
