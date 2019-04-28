package com.example.busproject.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.busproject.Model.Line;
import com.example.busproject.Model.Station;
import com.example.busproject.R;

import java.util.ArrayList;
import java.util.List;

import static com.loopj.android.http.AsyncHttpClient.log;


public class ShowLineAdapter extends RecyclerView.Adapter<ShowLineAdapter.MyViewHolder> {


    Activity activity;
    List<Line> lines;

    private OnItemClickListener mListener;

    Line line;
    StationAdapter stationAdapter;

    Location mlocation;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_line, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        line = lines.get(position);

        holder.txtStartPoint.setText(line.getStations().get(line.getStations().size() - 1).getName());
        holder.txtEndPoint.setText(line.getStations().get(0).getName());
        List<Station> stations = new ArrayList<>();

        for (Station station : line.getStations()) {

            if (station.getName() != "" && station.getName() != holder.txtEndPoint.getText().toString() && station.getName() != holder.txtStartPoint.getText().toString()) {
                stations.add(station);
            }
        }
        stationAdapter = new StationAdapter(stations, line.getColor(), mlocation, activity);

        holder.linesRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        holder.linesRecycler.setAdapter(stationAdapter);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(true);

                mListener.onItemClick(v, position, lines.get(position));
            }
        });

        try {
//            holder.imgStation2.setBackgroundColor(Color.parseColor(line.getColor()));
//            holder.imgStation.setBackgroundColor(Color.parseColor(line.getColor()));
//
            Drawable background = holder.imgStation.getBackground();

            ((GradientDrawable) background).setColor(Color.parseColor(line.getColor()));
            ((GradientDrawable) background).setStroke(3, Color.parseColor(line.getColor()));

            Drawable background2 = holder.imgStation2.getBackground();

            ((GradientDrawable) background2).setColor(Color.parseColor(line.getColor()));
            ((GradientDrawable) background2).setStroke(3, Color.parseColor(line.getColor()));

//                ((GradientDrawable) background).setStroke(3, Color.parseColor(color));

            holder.view3.setBackgroundColor(Color.parseColor(line.getColor()));
            holder.view2.setBackgroundColor(Color.parseColor(line.getColor()));
            holder.view1.setBackgroundColor(Color.parseColor(line.getColor()));
            holder.view4.setBackgroundColor(Color.parseColor(line.getColor()));
            holder.view5.setBackgroundColor(Color.parseColor(line.getColor()));

        } catch (Exception e) {
            log.e("parisa", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        RecyclerView linesRecycler;
        RelativeLayout bt;
        CheckBox checkBox;
        TextView txtEndPoint, txtStartPoint;
        ImageView imgStation2, imgStation;
        View view5, view3, view4, view2, view1;

        public MyViewHolder(View view) {
            super(view);

            linesRecycler = view.findViewById(R.id.linesRecycler);
            bt = view.findViewById(R.id.bt);

            checkBox = view.findViewById(R.id.check);

            txtEndPoint = view.findViewById(R.id.txtEndPoint);

            txtStartPoint = view.findViewById(R.id.txtStartPoint);

            view5 = view.findViewById(R.id.view5);
            view2 = view.findViewById(R.id.view2);
            view1 = view.findViewById(R.id.view1);
            view3 = view.findViewById(R.id.view3);
            view4 = view.findViewById(R.id.view4);
            imgStation2 = view.findViewById(R.id.imgStation2);
            imgStation = view.findViewById(R.id.imgStation);


        }


    }

    public ShowLineAdapter(List<Line> lines, Location mlocation, Activity activity, OnItemClickListener onItemClickListener) {

        this.lines = lines;
        this.activity = activity;
        this.mListener = onItemClickListener;
        this.mlocation = mlocation;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, Object object);
    }


}
