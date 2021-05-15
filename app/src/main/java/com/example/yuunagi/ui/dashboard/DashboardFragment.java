package com.example.yuunagi.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.yuunagi.R;
import com.example.yuunagi.crawlers.BilibiliCrawler;
import com.example.yuunagi.utils.ImageManager;

import java.io.IOException;
import java.security.PublicKey;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView fansNumberText;
    private TextView uid;
    private TextView username;
    private final BilibiliCrawler bilibiliCrawler;
    private ImageView icon;
    private String userInput = "";
    private Integer thisUid = 5200237;


    public DashboardFragment() {
        this.bilibiliCrawler = BilibiliCrawler.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        runCrawler();
    }

    public void runCrawler() {
        try {
            bilibiliCrawler.crawlUserInfo(thisUid);
            dashboardViewModel.setFansNumber(bilibiliCrawler.getFansNumber());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getNextUser() {
        ++thisUid;
        runCrawler();
    }

    public void getPrevUser() {
        --thisUid;
        runCrawler();
    }

    public void getNextUserIntelligently() {
        do {
            getNextUser();
        } while (bilibiliCrawler.getIconUrl().equals("http://i0.hdslb.com/bfs/face/member/noface.jpg") || bilibiliCrawler.getIconUrl().equals(""));
    }

    public void getPrevUserIntelligently() {
        do {
            getPrevUser();
        } while (bilibiliCrawler.getIconUrl().equals("http://i0.hdslb.com/bfs/face/member/noface.jpg") || bilibiliCrawler.getIconUrl().equals(""));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        fansNumberText = root.findViewById(R.id.fans);
        Button getNextButton = root.findViewById(R.id.next);
        Button getPrevButton = root.findViewById(R.id.PREV);
        Button jumpButton = root.findViewById(R.id.jump);
        Button searchButton = root.findViewById(R.id.search);
        Button intelligentNextButton = root.findViewById(R.id.IntelligentNext);
        Button intelligentPrevButton = root.findViewById(R.id.IntelligentPrev);
        getNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextUser();
            }
        });
        getPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrevUser();
            }
        });
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastUid = thisUid;
                try {
                    thisUid = Integer.parseInt(userInput);
                    runCrawler();
                } catch (Exception e) {
                    thisUid = lastUid;
                    e.printStackTrace();
                }
            }
        });
        intelligentNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextUserIntelligently();
            }
        });
        intelligentPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrevUserIntelligently();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("userInput", userInput);
                Integer lastUid = thisUid;
                if (userInput.length() > 0) {
                    try {
                        bilibiliCrawler.crawlUidByUsername(userInput);
                        thisUid = bilibiliCrawler.getUid();
                        if (thisUid < 0)
                            thisUid = lastUid;
                        else runCrawler();
                    } catch (InterruptedException e) {
                        thisUid = lastUid;
                        e.printStackTrace();
                    }
                }
            }
        });
        icon = root.findViewById(R.id.icon);
        icon.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                final String[] items = new String[]{"保存图片"};
                final Bitmap[] bitmap = new Bitmap[1];
                Glide.with(getContext()).load(bilibiliCrawler.getIconUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        bitmap[0] = resource;
                    }
                });
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (bitmap[0] != null) {
                                    try {
                                        ImageManager.getInstance().saveImageToGallery(getContext(), bitmap[0]);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "bitmap为空", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        uid = root.findViewById(R.id.Uid);
        SearchView searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userInput = newText;
                return false;
            }
        });
        username = root.findViewById(R.id.Username);
        dashboardViewModel.getFansNumber().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(@Nullable Integer fansNumber) {
                fansNumberText.setText(getString(R.string.bilibili_fans) + ":" + bilibiliCrawler.getFansNumber());
                uid.setText(getString(R.string.bilibili_uid) + ":" + thisUid.toString());
                username.setText(bilibiliCrawler.getUsername());
                Glide.with(getContext()).load(bilibiliCrawler.getIconUrl()).into(icon);
            }
        });
        return root;
    }


}
