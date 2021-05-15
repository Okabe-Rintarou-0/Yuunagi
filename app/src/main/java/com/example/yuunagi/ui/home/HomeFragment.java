package com.example.yuunagi.ui.home;

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
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView cover;
    private TextView presentKeyword;
    private TextView title;
    private TextView bvId;
    private List<String> BvIdList;
    private BilibiliCrawler bilibiliCrawler;
    private Integer thisPageIndex = 1;
    private Integer thisBvIdIndex = -1;
    private String thisKeyword = "斋藤飞鸟";
    private String userInput = "";

    public void getNewBvIdList() {
        try {
            BvIdList = null;
            bilibiliCrawler.crawlVideoBVIdUrl(thisKeyword, thisPageIndex);
            BvIdList = bilibiliCrawler.getBvIdList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void getNextCover() throws IOException, InterruptedException {
        if (++thisBvIdIndex >= BvIdList.size()) {
            ++thisPageIndex;
            thisBvIdIndex = 0;
            getNewBvIdList();
        }
//        Log.d("index", thisBvIdIndex.toString());
        String thisBvId = BvIdList.get(thisBvIdIndex);
        bilibiliCrawler.crawlVideoInfoBvId(thisBvId);
        Glide.with(getContext()).load(bilibiliCrawler.getVideoInfo().get("cover")).into(cover);
        title.setText(getString(R.string.video_title) + ":" + bilibiliCrawler.getVideoInfo().get("title"));
        bvId.setText(getString(R.string.video_bvId) + ":" + thisBvId);
    }

    @SuppressLint("SetTextI18n")
    public void getPrevCover() throws IOException, InterruptedException {
        if (thisBvIdIndex == 0) {
            if (thisPageIndex == 1) return;
            else {
                --thisPageIndex;
                getNewBvIdList();
                thisBvIdIndex = BvIdList.size();
            }
        }
        String thisBvId = BvIdList.get(--thisBvIdIndex);
        bilibiliCrawler.crawlVideoInfoBvId(thisBvId);
        Glide.with(getContext()).load(bilibiliCrawler.getVideoInfo().get("cover")).into(cover);
        title.setText(getString(R.string.video_title) + ":" + bilibiliCrawler.getVideoInfo().get("title"));
        bvId.setText(getString(R.string.video_bvId) + ":" + thisBvId);
    }


    @Override
    public void onStart() {
        super.onStart();
        bilibiliCrawler = BilibiliCrawler.getInstance();
        try {
            getNewBvIdList();
            getNextCover();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button getNextCoverButton = root.findViewById(R.id.getNextCoverButton);
        Button getPrevCoverButton = root.findViewById(R.id.getPrevCoverButton);
        getPrevCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getPrevCover();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        getNextCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getNextCover();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        SearchView coverSearchBar = root.findViewById(R.id.coverSearchBar);
        coverSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userInput = newText;
                Log.d("userInput", userInput);
                return false;
            }
        });
        title = root.findViewById(R.id.videoTitle);
        bvId = root.findViewById(R.id.BvId);
        presentKeyword = root.findViewById(R.id.keyword);
        presentKeyword.setText(getString(R.string.present_keyword) + ":" + "斋藤飞鸟");
        Button searchButton = root.findViewById(R.id.coverSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisKeyword = userInput;
                if (!thisKeyword.isEmpty()) {
                    try {
                        presentKeyword.setText(getString(R.string.present_keyword) + ":" + thisKeyword);
                        getNewBvIdList();
                        getNextCover();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cover = root.findViewById(R.id.videoCover);
        cover.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                final String[] items = new String[]{"保存图片"};
                final Bitmap[] bitmap = new Bitmap[1];
                Glide.with(getContext()).load(bilibiliCrawler.getVideoInfo().get("cover")).asBitmap().into(new SimpleTarget<Bitmap>() {
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


        return root;
    }
}
