package com.wakeparkby.Activity.SeasonTickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wakeparkby.HTTPController.SeasonTicketHistory;
import com.wakeparkby.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SeasonTicketsAdapter extends RecyclerView.Adapter<SeasonTicketsAdapter.NewsViewHolder> {

    Context mContext;
    List<SeasonTicketHistory> mData;

    public SeasonTicketsAdapter(Context mContext, List<SeasonTicketHistory> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        if (mData.get(viewType).getStatus().equals("ADDED")) {
            layout = LayoutInflater.from(mContext).inflate(R.layout.item_season_ticket_card_added, parent, false);
        } else {
            layout = LayoutInflater.from(mContext).inflate(R.layout.item_season_ticket_card_wrote_off, parent, false);
        }
        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.relativeLayoutCardView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        holder.tv_date.setText(mData.get(position).getDate());
        holder.tv_time.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        TextView tv_time;
        RelativeLayout relativeLayoutCardView;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayoutCardView = itemView.findViewById(R.id.relativeLayoutCardViewSeasonTicketsHistory);
            tv_date = itemView.findViewById(R.id.textViewSeasonTicketsHistoryDate);
            tv_time = itemView.findViewById(R.id.textViewSeasonTicketsHistoryTime);
        }
    }
}
