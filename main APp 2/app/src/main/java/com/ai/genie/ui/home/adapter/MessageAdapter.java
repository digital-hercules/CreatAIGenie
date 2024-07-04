package com.ai.genie.ui.home.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    ArrayList<LocalScrollViewModel> messageList;
    Context context;
    private final ClickListner listener;

    public MessageAdapter(Context context, ArrayList<LocalScrollViewModel> messageList, ClickListner listener) {
        this.context = context;
        this.messageList = messageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LocalScrollViewModel message = messageList.get(position);
        if (message.question.equals("pdf")){
            holder.card_view.setVisibility(View.VISIBLE);
            holder.tvQuestion.setVisibility(View.GONE);
        }else {
            holder.card_view.setVisibility(View.GONE);
            holder.tvQuestion.setVisibility(View.VISIBLE);
            holder.tvQuestion.setText(message.question);

        }
        if (messageList.size()-1 ==position){
            holder.llRegenerate.setVisibility(View.VISIBLE);
            holder.tvAnswer.setText(message.answer);

           /* if (NewChatActivity.startTextType) {
                Log.e("abc","=========message.answer.length()=============="+message.answer.length());
                holder.tvAnswer.animateText((Activity) context, message.answer);

            }else {
                holder.tvAnswer.stops();
                holder.tvAnswer.setText(message.answer);

            }
*/
        }else {
            holder.tvAnswer.setText(message.answer);
            holder.llRegenerate.setVisibility(View.GONE);

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
        private ImageView ivUser;
        private TextView tvQuestion;
//        private TextView tvAnswer;
        private TextView tvAnswer;

        private LinearLayout llRegenerate;
        private LinearLayout llCopy;
        private LinearLayout llSpeak;
        private CardView card_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.ivUser);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            llRegenerate = itemView.findViewById(R.id.llRegenerate);
            llCopy = itemView.findViewById(R.id.llCopy);
            llSpeak = itemView.findViewById(R.id.llSpeak);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
