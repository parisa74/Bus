package com.example.busproject.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.busproject.Model.Station;
import com.example.busproject.R;

import java.util.List;


public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {


    Context mContext;
    Activity activity;
    List<Station> stations;

    Location mLocation;

    String color;

    int r, g, b;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_item, parent, false);


        return new MyViewHolder(v);
    }

    @SuppressLint({"Range", "ResourceType"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Station station = stations.get(position);

        holder.stationName.setText(station.getName());//todo station.getName()

        Drawable background = holder.stationImg.getBackground();


        //((GradientDrawable) background).setStroke(3,Color.rgb(34,139,34));


//        ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, R.color.red));
//        ((GradientDrawable) background).setStroke(3,Color.rgb(34,139,34));

//       try {
//
//           ((GradientDrawable) background).setStroke(3, Color.parseColor(color));
//       }catch (Exception e){}



            if (NearestStationToMe(station, mLocation)) {

                ((GradientDrawable) background).setColor(Color.parseColor(color));
                ((GradientDrawable) background).setStroke(3, Color.parseColor(color));

            } else {
                ((GradientDrawable) background).setColor(Color.parseColor("#FFFFFF"));

                ((GradientDrawable) background).setStroke(3, Color.parseColor(color));
            }




//       try {
//
//           if (background instanceof ShapeDrawable) {
//               ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, Integer.parseInt(color)));
//           } else if (background instanceof GradientDrawable) {
//               ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, R.color.red));
//               ((GradientDrawable) background).setStroke(3,Color.rgb(34,139,34));
//           } else if (background instanceof ColorDrawable) {
//               ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, Integer.parseInt(color)));
//           }
//       }catch (Exception e){
//           Log.e("parisa","");
//       }

    }

    @Override
    public int getItemCount() {
        return stations.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView stationName;
        ImageView stationImg;

        public MyViewHolder(View view) {
            super(view);


            stationImg = view.findViewById(R.id.imgStation3);
            stationName = view.findViewById(R.id.txtStationName);


        }


    }

    public StationAdapter(List<Station> stations, String color, Location mLocation, Activity activity) {

        this.stations = stations;
        this.activity = activity;
        this.mLocation = mLocation;
        this.color = color;
    }

    public Boolean NearestStationToMe(Station station, Location mLocation) {

        Location stationLoc = new Location("");
        stationLoc.setLatitude(station.getLat());
        stationLoc.setLongitude(station.getLng());

//todo 10 should change to alarmDist
        if (stationLoc.distanceTo(mLocation) < 400) {
            return true;
        } else
            return false;


    }


}
