package com.example.youtubeui;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class VideoData {

    public static List<Video> getVideos() {
        List<Video> videos = new ArrayList<>();
        videos.add(new Video(R.string.v1_title, R.string.v1_channel, R.string.v1_views, R.string.v1_age,
                "1:02:14", R.string.chip_java, R.drawable.thumb_java, Color.parseColor("#3F51B5")));
        videos.add(new Video(R.string.v2_title, R.string.v2_channel, R.string.v2_views, R.string.v2_age,
                "18:45", R.string.chip_android, R.drawable.thumb_android, Color.parseColor("#009688")));
        videos.add(new Video(R.string.v3_title, R.string.v3_channel, R.string.v3_views, R.string.v3_age,
                "3:00:00", R.string.chip_music, R.drawable.thumb_lofi, Color.parseColor("#795548")));
        videos.add(new Video(R.string.v4_title, R.string.v4_channel, R.string.v4_views, R.string.v4_age,
                "12:31", R.string.chip_android, R.drawable.thumb_constraint, Color.parseColor("#009688")));
        videos.add(new Video(R.string.v5_title, R.string.v5_channel, R.string.v5_views, R.string.v5_age,
                "14:09", R.string.chip_martial, R.drawable.thumb_bjj, Color.parseColor("#E53935")));
        videos.add(new Video(R.string.v6_title, R.string.v6_channel, R.string.v6_views, R.string.v6_age,
                "10:00", R.string.chip_live, R.drawable.thumb_live, Color.parseColor("#3F51B5")));
        videos.add(new Video(R.string.v7_title, R.string.v7_channel, R.string.v7_views, R.string.v7_age,
                "27:50", R.string.chip_java, R.drawable.thumb_poo, Color.parseColor("#FB8C00")));
        videos.add(new Video(R.string.v8_title, R.string.v8_channel, R.string.v8_views, R.string.v8_age,
                "9:12", R.string.chip_games, R.drawable.thumb_games, Color.parseColor("#8E24AA")));
        return videos;
    }
}
