package com.ai.genie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;

import java.util.ArrayList;


public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder>{

    private ArrayList<String> itemList ;
    private Context context;
    private ClickListner listner;
    public SubCatAdapter(Context context, ArrayList<String> itemList, ClickListner listner) {
        this.itemList = itemList;
        this.context =  context;
        this.listner =  listner;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_title.setText(itemList.get(position));
        holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onItemClick(position,itemList.get(position));
            /*    Intent intent = new Intent(context, NewChatActivity.class);
                intent.putExtra("cat_id",itemList.get(position));
                intent.putExtra("type",itemList.get(position));
                context.startActivity(intent);*/
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }


}
