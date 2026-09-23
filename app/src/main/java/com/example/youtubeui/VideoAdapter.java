package com.example.youtubeui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }

    private final List<Video> videos;
    private final OnVideoClickListener listener;

    public VideoAdapter(List<Video> videos, OnVideoClickListener listener) {
        this.videos = videos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = videos.get(position);
        Context context = holder.itemView.getContext();
        String channel = context.getString(video.channelRes);
        holder.imgThumb.setImageResource(video.thumbRes);
        holder.txtDuration.setText(video.duration);
        holder.txtTitle.setText(video.titleRes);
        holder.txtMeta.setText(context.getString(R.string.video_meta,
                channel, context.getString(video.viewsRes), context.getString(video.ageRes)));
        holder.txtAvatar.setText(channel.substring(0, 1).toUpperCase(Locale.getDefault()));
        holder.txtAvatar.setBackgroundTintList(ColorStateList.valueOf(video.avatarColor));
        holder.itemView.setOnClickListener(v -> listener.onVideoClick(video));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class VideoHolder extends RecyclerView.ViewHolder {
        final ImageView imgThumb;
        final TextView txtDuration;
        final TextView txtAvatar;
        final TextView txtTitle;
        final TextView txtMeta;

        VideoHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtAvatar = itemView.findViewById(R.id.txtAvatar);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMeta = itemView.findViewById(R.id.txtMeta);
        }
    }
}
