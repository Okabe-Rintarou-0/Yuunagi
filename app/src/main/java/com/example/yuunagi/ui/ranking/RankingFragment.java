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

    @Override
    public void onStart() {
        super.onStart();
        bilibiliCrawler = BilibiliCrawler.getInstance();
        try {
            bilibiliCrawler.crawlRankInfo();
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
        ListView rankListView = root.findViewById(R.id.listview);
        rankList = new ArrayList<>();
        rankList.add(new Rank("http://i0.hdslb.com/bfs/archive/ff853403fb361763ac9c667a7dc99ae990700d44.jpg", "1", "1"));
        rankList.add(new Rank("http://i0.hdslb.com/bfs/archive/ff853403fb361763ac9c667a7dc99ae990700d44.jpg", "1", "1"));
        rankList.add(new Rank("http://i0.hdslb.com/bfs/archive/ff853403fb361763ac9c667a7dc99ae990700d44.jpg", "1", "1"));

        RankAdapter rankAdapter = new RankAdapter(getContext(), R.layout.rank_item, rankList);
        rankListView.setAdapter(rankAdapter);
        return root;
    }
}