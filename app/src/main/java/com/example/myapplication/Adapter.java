package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Meeting> meetingList;
    public Adapter(Context context, List<Meeting>meetingList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.meetingList = meetingList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.progressBar.setVisibility(View.VISIBLE);
            // Apply other styling as needed
        }
        Meeting meeting = meetingList.get(position);
        String time = meeting.getStartTime()+"-"+meeting.getEndTime();
        String date = meeting.getDate();
        String title = meeting.getTitle();
        String originator = "Originator: "+meeting.getOriginator();

        holder.time.setText(time);
        holder.date.setText(date);
        holder.title.setText(title);
        holder.originator.setText(originator);
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView date;
        TextView title;
        TextView originator;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeTvId);
            date = itemView.findViewById(R.id.dateTvId);
            title = itemView.findViewById(R.id.titleTvId);
            originator = itemView.findViewById(R.id.originatorTvId);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
