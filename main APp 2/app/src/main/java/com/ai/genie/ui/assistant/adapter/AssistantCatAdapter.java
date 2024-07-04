package com.ai.genie.ui.assistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;

import java.util.ArrayList;


public class AssistantCatAdapter extends RecyclerView.Adapter<AssistantCatAdapter.ViewHolder> {

    private ArrayList<String> itemList;
    private Context context;
    int selected = 0;
    private ClickListner listener;

    public AssistantCatAdapter(Context context, ArrayList<String> itemList, ClickListner listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assistant_cat, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvTitle.setText(itemList.get(position));
        if (selected == position) {
            holder.llMain.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded));
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.white_text));
        } else {
            holder.llMain.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_unselescted));
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = position;
                listener.onItemClick(position, itemList.get(position));
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private LinearLayout llMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }


}
