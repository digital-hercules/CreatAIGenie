package com.ai.genie.ui.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ai.genie.R;
import com.ai.genie.ui.home.model.SliderModel;
import com.ai.genie.util.AdsManager;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    private  ArrayList<SliderModel> mSliderItems;
    private Context context;
    // Constructor
    public SliderAdapter(Context context, ArrayList<SliderModel> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SliderModel sliderModel = mSliderItems.get(position);
        holder.tvTitle.setText(sliderModel.title);
        holder.tvDescription.setText(sliderModel.description);
        holder.llStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewChatActivity.class);
                intent.putExtra("cat_id", sliderModel.cat_id);
                intent.putExtra("type", sliderModel.type);
                intent.putExtra("assistant", sliderModel.assistant);
                intent.putExtra("system", sliderModel.system);
                context.startActivity(intent);
                AdsManager.showInterstitial((Activity) context);
            }
        });

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder {

        private TextView tvTitle;
        private TextView tvDescription;
        private LinearLayout llStart;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            llStart = itemView.findViewById(R.id.llStart);

        }
    }
}
