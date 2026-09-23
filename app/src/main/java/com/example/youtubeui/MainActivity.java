package com.example.youtubeui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Categoria de cada chip. Zero significa "Tudo" (sem filtro). */
    private static final int[][] CHIP_CATEGORIES = {
            {R.id.chipAll, 0},
            {R.id.chipJava, R.string.chip_java},
            {R.id.chipAndroid, R.string.chip_android},
            {R.id.chipMusic, R.string.chip_music},
            {R.id.chipLive, R.string.chip_live},
            {R.id.chipGames, R.string.chip_games},
            {R.id.chipMartial, R.string.chip_martial},
    };

    private final List<Video> allVideos = new ArrayList<>();
    private final List<Video> shownVideos = new ArrayList<>();
    private VideoAdapter adapter;

    private View chipScroll;
    private View recyclerVideos;
    private View youScreen;
    private View placeholderScreen;
    private BottomNavigationView bottomNav;
    private OnBackPressedCallback backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allVideos.addAll(VideoData.getVideos());
        shownVideos.addAll(allVideos);

        chipScroll = findViewById(R.id.chipScroll);
        recyclerVideos = findViewById(R.id.recyclerVideos);
        youScreen = findViewById(R.id.youScreen);
        placeholderScreen = findViewById(R.id.placeholderScreen);
        bottomNav = findViewById(R.id.bottomNav);

        setupToolbar();
        setupList();
        setupChips();
        setupYouScreen();
        setupBottomNav();
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void setupList() {
        RecyclerView recycler = findViewById(R.id.recyclerVideos);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(shownVideos, this::openVideo);
        recycler.setAdapter(adapter);
    }

    private void openVideo(Video video) {
        Intent intent = new Intent(this, WatchActivity.class);
        intent.putExtra(WatchActivity.EXTRA_VIDEO, video);
        startActivity(intent);
    }

    private void setupChips() {
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int category = 0;
            if (!checkedIds.isEmpty()) {
                category = categoryOf(checkedIds.get(0));
            }
            filterByCategory(category);
        });
    }

    private static int categoryOf(int chipId) {
        for (int[] pair : CHIP_CATEGORIES) {
            if (pair[0] == chipId) {
                return pair[1];
            }
        }
        return 0;
    }

    private void filterByCategory(int categoryRes) {
        shownVideos.clear();
        for (Video video : allVideos) {
            if (categoryRes == 0 || video.categoryRes == categoryRes) {
                shownVideos.add(video);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /** Tela "Você": cada linha só mostra um aviso, como no restante do protótipo. */
    private void setupYouScreen() {
        int[] rows = {R.id.rowHistory, R.id.rowPlaylists, R.id.rowVideos,
                R.id.rowDownloads, R.id.rowWatchLater};
        for (int id : rows) {
            View row = youScreen.findViewById(id);
            TextView label = (TextView) ((android.view.ViewGroup) row).getChildAt(1);
            row.setOnClickListener(v ->
                    Toast.makeText(this, label.getText(), Toast.LENGTH_SHORT).show());
        }
    }

    private void setupBottomNav() {
        // Na aba "Início" o botão Voltar sai do app. Nas outras, volta para o Início.
        backToHome = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backToHome);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showScreen(true, false, false);
            } else if (id == R.id.nav_you) {
                showScreen(false, true, false);
            } else {
                ((TextView) placeholderScreen.findViewById(R.id.txtPlaceholderTitle))
                        .setText(getString(R.string.coming_soon, item.getTitle()));
                showScreen(false, false, true);
            }
            backToHome.setEnabled(id != R.id.nav_home);
            return true;
        });
    }

    private void showScreen(boolean home, boolean you, boolean placeholder) {
        int homeVisibility = home ? View.VISIBLE : View.GONE;
        chipScroll.setVisibility(homeVisibility);
        recyclerVideos.setVisibility(homeVisibility);
        youScreen.setVisibility(you ? View.VISIBLE : View.GONE);
        placeholderScreen.setVisibility(placeholder ? View.VISIBLE : View.GONE);
    }
}
