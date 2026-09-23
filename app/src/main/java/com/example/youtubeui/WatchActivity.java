package com.example.youtubeui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WatchActivity extends AppCompatActivity {

    public static final String EXTRA_VIDEO = "video";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private SeekBar seekBar;
    private TextView txtTime;
    private ImageButton btnPlay;
    private boolean playing = true;
    private int totalSeconds;
    private boolean liked = false;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (playing && seekBar.getProgress() < totalSeconds) {
                seekBar.setProgress(seekBar.getProgress() + 1);
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        Video video = IntentCompat.getSerializableExtra(getIntent(), EXTRA_VIDEO, Video.class);
        if (video == null) {
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        ((ImageView) findViewById(R.id.imgPlayer)).setImageResource(video.thumbRes);
        setupPlayer(video);
        setupList(video);
    }

    private void setupPlayer(Video video) {
        seekBar = findViewById(R.id.seekBar);
        txtTime = findViewById(R.id.txtTime);
        btnPlay = findViewById(R.id.btnPlay);

        totalSeconds = parseSeconds(video.duration);
        seekBar.setMax(totalSeconds);
        seekBar.setProgress(totalSeconds / 5);
        updateTime(seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                updateTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });

        btnPlay.setOnClickListener(v -> {
            playing = !playing;
            btnPlay.setImageResource(playing ? R.drawable.ic_pause : R.drawable.ic_play);
        });
    }

    private void setupList(Video video) {
        ListView list = findViewById(R.id.listRelated);
        View header = getLayoutInflater().inflate(R.layout.header_watch, list, false);
        bindHeader(header, video);
        list.addHeaderView(header, null, false);

        List<Video> related = new ArrayList<>(VideoData.getVideos());
        related.removeIf(v -> v.titleRes == video.titleRes);
        list.setAdapter(new VideoListAdapter(this, related));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Video next = (Video) parent.getItemAtPosition(position);
            if (next != null) {
                getIntent().putExtra(EXTRA_VIDEO, next);
                recreate();
            }
        });
    }

    private void bindHeader(View header, Video video) {
        String channel = getString(video.channelRes);
        ((TextView) header.findViewById(R.id.txtWatchTitle)).setText(video.titleRes);
        ((TextView) header.findViewById(R.id.txtWatchMeta)).setText(
                getString(R.string.watch_meta, getString(video.viewsRes), getString(video.ageRes)));
        ((TextView) header.findViewById(R.id.txtWatchChannel)).setText(channel);
        TextView avatar = header.findViewById(R.id.txtWatchAvatar);
        avatar.setText(channel.substring(0, 1).toUpperCase(Locale.getDefault()));
        avatar.setBackgroundTintList(ColorStateList.valueOf(video.avatarColor));

        Button btnSubscribe = header.findViewById(R.id.btnSubscribe);
        btnSubscribe.setOnClickListener(v -> {
            boolean subscribed = btnSubscribe.getText().toString()
                    .equals(getString(R.string.subscribed));
            btnSubscribe.setText(subscribed ? R.string.subscribe : R.string.subscribed);
            btnSubscribe.setBackgroundTintList(ColorStateList.valueOf(
                    subscribed ? Color.parseColor("#0F0F0F") : Color.parseColor("#909090")));
        });

        Button btnLike = header.findViewById(R.id.btnLike);
        btnLike.setOnClickListener(v -> {
            liked = !liked;
            btnLike.setText(liked ? R.string.likes_count : R.string.like);
            btnLike.setBackgroundTintList(ColorStateList.valueOf(
                    liked ? Color.parseColor("#D9D9D9") : Color.parseColor("#F2F2F2")));
        });

        header.findViewById(R.id.btnShare).setOnClickListener(v -> toast(R.string.share));
        header.findViewById(R.id.btnDownload).setOnClickListener(v -> toast(R.string.download));
        header.findViewById(R.id.btnSave).setOnClickListener(v -> toast(R.string.save));

        EditText etComment = header.findViewById(R.id.etComment);
        etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String text = etComment.getText().toString().trim();
                if (!text.isEmpty()) {
                    Toast.makeText(this, getString(R.string.comment_sent, text), Toast.LENGTH_SHORT).show();
                    etComment.setText("");
                }
                return true;
            }
            return false;
        });
    }

    private void toast(int stringRes) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show();
    }

    private void updateTime(int seconds) {
        txtTime.setText(format(seconds) + " / " + format(totalSeconds));
    }

    private static String format(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        if (h > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", h, m, s);
        }
        return String.format(Locale.US, "%d:%02d", m, s);
    }

    private static int parseSeconds(String duration) {
        String[] parts = duration.split(":");
        int total = 0;
        for (String part : parts) {
            total = total * 60 + Integer.parseInt(part);
        }
        return total;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(tick, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(tick);
    }
}
