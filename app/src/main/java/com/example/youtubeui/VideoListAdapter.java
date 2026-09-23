package com.example.youtubeui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class VideoListAdapter extends ArrayAdapter<Video> {

    public VideoListAdapter(@NonNull Context context, @NonNull List<Video> videos) {
        super(context, 0, videos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_video, parent, false);
        }
        Video video = getItem(position);
        String channel = getContext().getString(video.channelRes);
        ((ImageView) view.findViewById(R.id.imgThumb)).setImageResource(video.thumbRes);
        ((TextView) view.findViewById(R.id.txtDuration)).setText(video.duration);
        ((TextView) view.findViewById(R.id.txtTitle)).setText(video.titleRes);
        ((TextView) view.findViewById(R.id.txtMeta)).setText(getContext().getString(
                R.string.video_meta, channel,
                getContext().getString(video.viewsRes), getContext().getString(video.ageRes)));
        TextView avatar = view.findViewById(R.id.txtAvatar);
        avatar.setText(channel.substring(0, 1).toUpperCase(Locale.getDefault()));
        avatar.setBackgroundTintList(ColorStateList.valueOf(video.avatarColor));
        return view;
    }
}
