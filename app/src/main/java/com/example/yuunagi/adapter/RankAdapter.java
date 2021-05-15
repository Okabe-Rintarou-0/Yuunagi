package com.example.yuunagi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.yuunagi.R;
import com.example.yuunagi.entity.Rank;

import java.util.List;

public class RankAdapter extends ArrayAdapter<Rank> {
    private int resourceId;

    public RankAdapter(@NonNull Context context, int resource, @NonNull List<Rank> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Rank rank = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView rankCover = view.findViewById(R.id.rank_image);
        TextView rankTitle = view.findViewById(R.id.rank_title);
        TextView rankScore = view.findViewById(R.id.rank_score);
        Glide.with(getContext()).load(rank.getCoverUrl()).into(rankCover);
        rankTitle.setText(rank.getBangumiTitle());
        rankScore.setText(rank.getScore());
        return view;
    }
}
