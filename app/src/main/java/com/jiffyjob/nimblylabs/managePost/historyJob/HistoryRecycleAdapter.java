package com.jiffyjob.nimblylabs.managePost.historyJob;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiffyjob.nimblylabs.app.R;

import java.util.List;

/**
 * Created by himur on 9/7/2016.
 */
public class HistoryRecycleAdapter extends RecyclerView.Adapter<HistoryRecycleAdapter.ViewHolder> {

    public HistoryRecycleAdapter(List<String> historyList) {
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = historyList.get(position);
        holder.contentTV.setText(content);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contentTV;
        public View highlight;

        public ViewHolder(View view) {
            super(view);
            contentTV = (TextView) view.findViewById(R.id.contentTV);
            highlight = view.findViewById(R.id.highlight);
        }
    }

    private List<String> historyList;
}
