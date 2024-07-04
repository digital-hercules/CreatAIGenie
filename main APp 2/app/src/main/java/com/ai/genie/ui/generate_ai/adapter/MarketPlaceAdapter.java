package com.ai.genie.ui.generate_ai.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.adapter.SubCatAdapter;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.models.ItemModel;
import com.ai.genie.ui.generate_ai.model.MarketModel;
import com.ai.genie.ui.home.view.NewChatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class MarketPlaceAdapter extends RecyclerView.Adapter<MarketPlaceAdapter.ViewHolder>{

    private ArrayList<MarketModel> itemList ;
    private Context context;
    public MarketPlaceAdapter(Context context, ArrayList<MarketModel> itemList) {
        this.itemList = itemList;
        this.context =  context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MarketModel itemModel = itemList.get(position);
        holder.tv_title.setText(itemModel.title);
        Log.e("abc","==========itemModel.id======"+itemModel.id);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("abc","==========itemModel.id======"+itemModel.id);
                /*if (itemModel.bottomSheet){
                    showSubCat(itemModel);
                }else {*/
                    Intent intent = new Intent(context, NewChatActivity.class);
                    intent.putExtra("cat_id", itemModel.cat_id);
                    intent.putExtra("type", itemModel.type);
                    intent.putExtra("assistant", itemModel.assistant);
                    intent.putExtra("system", itemModel.system);
                    context.startActivity(intent);
                /*}*/
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
        private LinearLayout llMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            card_view = itemView.findViewById(R.id.card_view);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }

    public void showSubCat(ItemModel itemModel ){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.sub_cat_dialog);
        RecyclerView rvItem = bottomSheetDialog.findViewById(R.id.rvItem);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new GridLayoutManager(context,1));
        SubCatAdapter adapterSports = new SubCatAdapter(context, itemModel.list, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {

            }

            @Override
            public void onItemClick(int pos, String question) {
                Intent intent = new Intent(context, NewChatActivity.class);
                intent.putExtra("cat_id",question);
                intent.putExtra("type",question);
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        rvItem.setAdapter(adapterSports);
        bottomSheetDialog.show();
    }
}
