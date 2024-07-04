package com.ai.genie.ui.home.adapter;

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
import com.ai.genie.localdatabase.LocalScrollViewModel;

import java.util.ArrayList;

public class MessageTextToSpeechAdapter extends RecyclerView.Adapter<MessageTextToSpeechAdapter.ViewHolder> {
    ArrayList<LocalScrollViewModel> messageList;
    Context context;
    private final ClickListner listener;

    public MessageTextToSpeechAdapter(Context context, ArrayList<LocalScrollViewModel> messageList, ClickListner listener) {
        this.context = context;
        this.messageList = messageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_to_speech, null);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalScrollViewModel localScrollViewModel = messageList.get(position);
/*
        if (messageList.size()-1 ==position){
            holder.llRegenerate.setVisibility(View.VISIBLE);

        }else {
            holder.llRegenerate.setVisibility(View.GONE);

        }*/
        holder.tvQuestion.setText(localScrollViewModel.question);
        if (localScrollViewModel.answer.contains("http")){
            holder.llSound.setVisibility(View.VISIBLE);
            holder.tvAnswer.setVisibility(View.GONE);
        }else {
            holder.llSound.setVisibility(View.GONE);
            holder.tvAnswer.setVisibility(View.VISIBLE);
            holder.tvAnswer.setText(localScrollViewModel.answer);

        }

        holder.llSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickCopy(position,localScrollViewModel.answer);
            }
        });

        holder.llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,localScrollViewModel.answer);

//                listener.onItemClickCopy(position,messageList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuestion;
        private TextView tvAnswer;

        private LinearLayout llSpeech;
        private LinearLayout llDownload;
        private LinearLayout llSound;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);

            llSpeech = itemView.findViewById(R.id.llSpeech);
            llDownload = itemView.findViewById(R.id.llDownload);
            llSound = itemView.findViewById(R.id.llSound);
        }
    }
}
