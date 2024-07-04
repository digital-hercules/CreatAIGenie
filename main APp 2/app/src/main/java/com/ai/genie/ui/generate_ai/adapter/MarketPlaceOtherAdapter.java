package com.ai.genie.ui.generate_ai.adapter;

import static com.ai.genie.ui.home.view.HomeFragment.display_delete;
import static com.ai.genie.ui.home.view.HomeFragment.tvClear;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.CatModel;
import com.ai.genie.ui.home.view.NewChatActivity;

import java.util.ArrayList;


public class MarketPlaceOtherAdapter extends RecyclerView.Adapter<MarketPlaceOtherAdapter.ViewHolder>{

    private ArrayList<CatModel> itemList ;
    private Context context;
    private ClickListner listener;
    public MarketPlaceOtherAdapter(Context context, ArrayList<CatModel> itemList, ClickListner listener) {
        this.itemList = itemList;
        this.context =  context;
        this.listener =  listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CatModel itemModel = itemList.get(position);
        Log.e("abc","=======itemModel.category_name==========="+itemModel.category_name);
        Log.e("abc","=======itemModel.category_id==========="+itemModel.category_id);
        holder.tv_title.setText(itemModel.category_name);
        if (position %2==0){
            holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
        }else {
            holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.color2));

        }

        if (display_delete){
            holder.ivDelete.setVisibility(View.VISIBLE);
            tvClear.setVisibility(View.VISIBLE);
        }else {
            holder.ivDelete.setVisibility(View.GONE);
            tvClear.setVisibility(View.GONE);

        }
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewChatActivity.class);
                intent.putExtra("cat_id",itemModel.category_id);
                intent.putExtra("type",itemModel.category_name);
                context.startActivity(intent);
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                display_delete = true;
                notifyDataSetChanged();
                return false;
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,itemModel.category_id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private CardView card_view;
        private ImageView ivDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            card_view = itemView.findViewById(R.id.card_view);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
