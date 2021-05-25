package com.example.yuunagi.ui.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.yuunagi.R;
import com.example.yuunagi.adapter.RankAdapter;
import com.example.yuunagi.crawlers.BilibiliCrawler;
import com.example.yuunagi.entity.Rank;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {

    private RankingViewModel rankingViewModel;
    private List<Rank> rankList;
    private BilibiliCrawler bilibiliCrawler;
    private ListView rankListView;
    private RankAdapter rankAdapter;

    @Override
    public void onStart() {
        super.onStart();
        bilibiliCrawler = BilibiliCrawler.getInstance();
        try {
            bilibiliCrawler.crawlRankInfo();
            rankList = bilibiliCrawler.getRankList();
            rankAdapter = new RankAdapter(getContext(), R.layout.rank_item, rankList);
            rankListView.setAdapter(rankAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rankingViewModel =
                ViewModelProviders.of(this).get(RankingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ranking, container, false);
        rankListView = root.findViewById(R.id.listview);
//        rankList = new ArrayList<>();
//        rankList.add(new Rank("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F1f2d43c9129520f5e595ab7d5b7eae8fe64b009d.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623676639&t=cd394260d8dbd4c4a9e4f1b429c19f08", "1", "1"));
        return root;
    }
}