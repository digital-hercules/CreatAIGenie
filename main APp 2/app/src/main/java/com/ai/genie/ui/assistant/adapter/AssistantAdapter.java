package com.ai.genie.ui.assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.adapter.SubCatAdapter;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.ui.assistant.model.AssistantModel;
import com.ai.genie.models.ItemModel;
import com.ai.genie.ui.assistant.view.AssistantChatActivity;
import com.ai.genie.ui.home.view.NewChatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.ViewHolder> {

    private ArrayList<AssistantModel> itemList;
    private Context context;

    public AssistantAdapter(Context context, ArrayList<AssistantModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assistant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AssistantModel itemModel = itemList.get(position);
        holder.tvTitle.setText(itemModel.title);
        holder.tvDescription.setText(itemModel.description);

        Glide.with(context).load(itemModel.image).into(holder.ivImage);
        Log.e("abc", "==========itemModel.id======" + itemModel.id);

        holder.llMain.setOnClickListener(view -> {
            Intent intent = new Intent(context, AssistantChatActivity.class);
            intent.putExtra("itemModel", itemModel);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDescription;
        private LinearLayout llMain;
        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }

    public void showSubCat(ItemModel itemModel) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.sub_cat_dialog);
        RecyclerView rvItem = bottomSheetDialog.findViewById(R.id.rvItem);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new GridLayoutManager(context, 1));
        SubCatAdapter adapterSports = new SubCatAdapter(context, itemModel.list, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {

            }

            @Override
            public void onItemClick(int pos, String question) {
                Intent intent = new Intent(context, NewChatActivity.class);
                intent.putExtra("cat_id", question);
                intent.putExtra("type", question);
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        rvItem.setAdapter(adapterSports);
        bottomSheetDialog.show();
    }
}
