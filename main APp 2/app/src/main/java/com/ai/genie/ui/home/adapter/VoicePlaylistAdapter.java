package com.ai.genie.ui.home.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.ui.home.model.VoicePlaylistModel;
import com.ai.genie.ui.home.view.TextToSpeechActivity;

import java.util.ArrayList;

public class VoicePlaylistAdapter extends RecyclerView.Adapter<VoicePlaylistAdapter.ViewHolder>{

    private ArrayList<VoicePlaylistModel> itemList ;
    private Context context;
    private final ClickListner listener;

    public VoicePlaylistAdapter(Context context, ArrayList<VoicePlaylistModel> itemList, ClickListner listener) {
        this.itemList = itemList;
        this.context =  context;
        this.listener =  listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_play_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoicePlaylistModel model = itemList.get(position);
        holder.tvName.setText(model.name);

        if (TextToSpeechActivity.selectVoice==position){
            holder.llMain.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.sub_color)));
        }else {
            holder.llMain.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));

        }
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,model.id);
                notifyDataSetChanged();
            }
        });

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private LinearLayout llMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            llMain = itemView.findViewById(R.id.llMain);

        }
    }
}
