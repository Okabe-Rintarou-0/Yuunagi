package com.example.yuunagi.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.example.yuunagi.crawlers.FansMonitor;
import com.example.yuunagi.utils.ImageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import im.dacer.androidcharts.LineView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView fansNumberText;
    private TextView uid;
    private TextView username;
    private final BilibiliCrawler bilibiliCrawler;
    private ImageView icon;
    private String userInput = "";
    private Integer thisUid = 5200237;
    ArrayList<String> bottomLabel = new ArrayList<>();
    ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
    private Integer fansNumberWhenStartMonitor;
    List<PointValue> fansNumberRecord = new ArrayList<>();
    LineChartView mChartView;

    private void addRecord(Float newFansNumber) {
        int currentSize = fansNumberRecord.size();
        if (currentSize > 0 && newFansNumber == fansNumberRecord.get(currentSize - 1).getY()) {
            return;
        }
        if (currentSize == 8) {
            for (int i = 0; i < 7; ++i)
                fansNumberRecord.set(i, new PointValue(i, fansNumberRecord.get(i + 1).getY()));
            fansNumberRecord.set(7, new PointValue(7, newFansNumber));
        } else
            fansNumberRecord.add(new PointValue(currentSize, newFansNumber));
        Log.d("record", fansNumberRecord.toString());
        drawChart();
    }

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
            fansNumberWhenStartMonitor = Integer.parseInt(bilibiliCrawler.getFansNumber());
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

    private void drawChart() {
        Line line = new Line(fansNumberRecord).setColor(Color.parseColor("#FFCD41"));
        line.setCubic(false);//设置是平滑的还是直的
        line.setFilled(true);
        line.setShape(ValueShape.CIRCLE);
        line.setHasLabels(true);
        line.setHasPoints(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        mChartView.setInteractive(true);//设置图表是可以交互的（拖拽，缩放等效果的前提）
        mChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);//设置缩放方向
        LineChartData data = new LineChartData();
        Axis axisX = new Axis();//x轴
        Axis axisY = new Axis();//y轴
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setLines(lines);
        mChartView.setLineChartData(data);//给图表设置数据
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
        dashboardViewModel.getFansNumber().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(@Nullable String fansNumber) {
                fansNumberText.setText(getString(R.string.bilibili_fans) + ":" + bilibiliCrawler.getFansNumber());
                uid.setText(getString(R.string.bilibili_uid) + ":" + thisUid.toString());
                username.setText(bilibiliCrawler.getUsername());
                Glide.with(getContext()).load(bilibiliCrawler.getIconUrl()).into(icon);
            }
        });
        mChartView = root.findViewById(R.id.chart);
        Button monitor;
        monitor = root.findViewById(R.id.monitor);
        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChartView.getVisibility() == View.INVISIBLE) {
                    mChartView.setVisibility(View.VISIBLE);
                    icon.setVisibility(View.INVISIBLE);
                    fansNumberWhenStartMonitor = Integer.parseInt(bilibiliCrawler.getFansNumber());
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            addRecord(Float.parseFloat(FansMonitor.getInstance().crawFansNumber(thisUid)));
                        }
                    };
                    timer.schedule(timerTask, 0, 5000);
                } else {
                    mChartView.setVisibility(View.INVISIBLE);
                    icon.setVisibility(View.VISIBLE);
                }
            }
        });
        return root;
    }
}
