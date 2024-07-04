package com.ai.genie.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.models.ViewpagerModel;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {


    private ArrayList<ViewpagerModel> itemList ;
    private Activity context;
    private final ClickListner listener;
    private String[] price ={"3000","6000","9000","12000","15000"};
    private String[] amount ={"4.50","9","13.50","18","22.50"};

    int select = 0;
    public ViewPagerAdapter(Activity context, ArrayList<ViewpagerModel> itemList, ClickListner listener) {
        this.itemList = itemList;
        this.context =  context;
        this.listener = listener;

    }
    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pager, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        ViewpagerModel model = itemList.get(position);
        if (model.plan==1){
            holder.rlProPlan.setVisibility(View.VISIBLE);
            holder.rlStarter.setVisibility(View.GONE);
            holder.llIndicator.setVisibility(View.VISIBLE);
            holder.rlWords.setVisibility(View.GONE);

            if (model.yearly){
                holder.switch1.setChecked(true);
                holder.tvProPrice.setText("$37");
                holder.tvProYearlyPrice.setVisibility(View.VISIBLE);
                holder.tvProSaving.setVisibility(View.VISIBLE);

            }else {
                holder.switch1.setChecked(false);
                holder.tvProPrice.setText("$49");
                holder.tvProYearlyPrice.setVisibility(View.GONE);
                holder.tvProSaving.setVisibility(View.GONE);
            }
        }else if (model.plan==2){
            holder.rlProPlan.setVisibility(View.GONE);
            holder.rlStarter.setVisibility(View.VISIBLE);
            holder.llIndicator.setVisibility(View.VISIBLE);
            holder.rlWords.setVisibility(View.GONE);

            if (model.yearly){
                holder.switch1.setChecked(true);
                holder.tvStarterPrice.setText("$15");
                holder.tvStarterYearlyPrice.setVisibility(View.VISIBLE);
                holder.tvStarterSaving.setVisibility(View.VISIBLE);

            }else {
                holder.switch1.setChecked(false);
                holder.tvStarterPrice.setText("$20");
                holder.tvStarterYearlyPrice.setVisibility(View.GONE);
                holder.tvStarterSaving.setVisibility(View.GONE);
            }
        }else if (model.plan==3){
            holder.rlProPlan.setVisibility(View.GONE);
            holder.rlStarter.setVisibility(View.GONE);
            holder.llIndicator.setVisibility(View.GONE);
            holder.rlWords.setVisibility(View.VISIBLE);
        }

        holder.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.yearly = b;
                notifyItemChanged(position);            }
        });

        holder.tvWords.setText(price[select]+" words");
        holder.tvWordPrice.setText("$"+amount[select]);
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select<price.length-1) {
                    select++;
                    holder.tvWords.setText(price[select] + " words");
                    holder.tvWordPrice.setText("$"+amount[select]);

                }

            }
        });
        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select>0) {
                    select--;
                    holder.tvWords.setText(price[select] + " words");
                    holder.tvWordPrice.setText("$"+amount[select]);

                }

            }
        });

        holder.tvProUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.yearly) {
                    listener.onItemClick(1,"37");
                }else {
                    listener.onItemClick(1,"49");
                }
            }
        });
        holder.tvStarterUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.yearly) {
                    listener.onItemClick(2,"15");
                }else {
                    listener.onItemClick(2,"20");
                }
            }
        });
        holder.tvWordUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(3,amount[select]);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlProPlan;
        private RelativeLayout rlStarter;
        private RelativeLayout rlWords;
        private LinearLayout llIndicator;
        private SwitchCompat switch1;
        private TextView tvProPrice;
        private TextView tvProYearlyPrice;
        private TextView tvProSaving;
        private TextView tvProUpgrade;
        private TextView tvStarterPrice;
        private TextView tvStarterYearlyPrice;
        private TextView tvStarterSaving;
        private TextView tvStarterUpgrade;
        private TextView tvWordUpgrade;
        private TextView tvWords;
        private TextView tvWordPrice;
        private ImageView ivAdd;
        private ImageView ivMinus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rlProPlan = itemView.findViewById(R.id.rlProPlan);
            rlStarter = itemView.findViewById(R.id.rlStarter);
            rlWords = itemView.findViewById(R.id.rlWords);
            llIndicator = itemView.findViewById(R.id.llIndicator);
            switch1 = itemView.findViewById(R.id.switch1);
            tvProPrice = itemView.findViewById(R.id.tvProPrice);
            tvProYearlyPrice = itemView.findViewById(R.id.tvProYearlyPrice);
            tvProSaving = itemView.findViewById(R.id.tvProSaving);
            tvProUpgrade = itemView.findViewById(R.id.tvProUpgrade);
            tvStarterPrice = itemView.findViewById(R.id.tvStarterPrice);
            tvStarterYearlyPrice = itemView.findViewById(R.id.tvStarterYearlyPrice);
            tvStarterSaving = itemView.findViewById(R.id.tvStarterSaving);
            tvStarterUpgrade = itemView.findViewById(R.id.tvStarterUpgrade);
            tvWordUpgrade = itemView.findViewById(R.id.tvWordUpgrade);
            tvWords = itemView.findViewById(R.id.tvWords);
            tvWordPrice = itemView.findViewById(R.id.tvWordPrice);
            ivAdd = itemView.findViewById(R.id.ivAdd);
            ivMinus = itemView.findViewById(R.id.ivMinus);

        }
    }
}
