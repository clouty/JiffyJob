package com.jiffyjob.nimblylabs.managePost.pendingJob;

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
public class PendingRecycleAdapter extends RecyclerView.Adapter<PendingRecycleAdapter.ViewHolder> {

    public PendingRecycleAdapter(List<String> pendingList) {
        this.pendingList = pendingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_job_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = pendingList.get(position);
        holder.contentTV.setText(content);
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
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

    private List<String> pendingList;
}
