package com.ai.genie.ui.home.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ChatWithImageAdapter extends RecyclerView.Adapter<ChatWithImageAdapter.ViewHolder>{
    ArrayList<LocalScrollViewModel> messageList;
    Context context;
    private final ClickListner listener;

    public ChatWithImageAdapter(Context context, ArrayList<LocalScrollViewModel> messageList, ClickListner listener) {
        this.context = context;
        this.messageList = messageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_with_image, null);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LocalScrollViewModel message = messageList.get(position);
        holder.tvQuestion.setText(message.question);
        holder.tvAnswer.setText(message.answer);
        if (message.image!=null) {
            Log.e("abc","============message.image=========="+message.image);
            if (message.image.contains("data:image/png;base64,")) {
                String image_url = message.image.replace("data:image/png;base64,", "");
                byte[] decodedString = Base64.decode(image_url, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.ivImage.setImageBitmap(decodedByte);
            } else {
                Glide.with(context).load(message.image).into(holder.ivImage);
            }
        }else {
            Log.e("abc","============message.image==null========");

            holder.ivImage.setVisibility(View.GONE);
        }

        holder.llRegenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,message.question);
            }
        });

        holder.llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text to copy", message.answer);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                myClipboard.setPrimaryClip(myClip);
            }
        });

        holder.llSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickCopy(position,message.answer);
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
        private ShapeableImageView ivImage;
        private LinearLayout llRegenerate;
        private LinearLayout llSpeak;
        private LinearLayout llCopy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            ivImage = itemView.findViewById(R.id.ivImage);
            llRegenerate = itemView.findViewById(R.id.llRegenerate);
            llSpeak = itemView.findViewById(R.id.llSpeak);
            llCopy = itemView.findViewById(R.id.llCopy);

        }
    }
}
