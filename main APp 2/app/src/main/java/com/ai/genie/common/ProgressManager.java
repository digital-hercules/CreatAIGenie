package com.ai.genie.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ai.genie.R;
import com.ai.genie.ui.sidenav.view.PlanActivity;
import com.ai.genie.util.AdsManager;

public class ProgressManager {
    public static AlertDialog deleteDialog;

    public static void showDailog(Activity activity) {

        LayoutInflater factory = LayoutInflater.from(activity);
        final View deleteDialogView = factory.inflate(R.layout.dialog_progress, null);
        deleteDialog = new android.app.AlertDialog.Builder(activity).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.setCancelable(false);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();
    }

    public static void dismissDialog() {
        if (deleteDialog != null) {
            deleteDialog.dismiss();
        }

    }

    public static void notify(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_confirmation, null, false);

        TextView tvYes = view.findViewById(R.id.tvYes);
        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);

        tvYes.setText("Send");
        tvTitle.setText("Development Mode!");
        tvMessage.setText("This feature is currently in development.Your feedback is valuable!If you have any suggestions, click on the 'Send' button. Thank you!");

        tvYes.setOnClickListener(view1 -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:tkalra12345@gmail.com?subject=" + Uri.encode("subject") + "&body=" + Uri.encode("body"));
            intent.setData(data);
            activity.startActivity(intent);
            dialog.dismiss();
        });
        tvNo.setOnClickListener(view1 -> {

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public static void showUpgradeDialog(Activity activity, String cat_id) {
        Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_upgrade, null, false);

        LinearLayout llUpgrade = view.findViewById(R.id.llUpgrade);
        LinearLayout llWatchAd = view.findViewById(R.id.llWatchAd);


        llUpgrade.setOnClickListener(view1 -> {

            Intent intent = new Intent(activity, PlanActivity.class);
            activity.startActivity(intent);
            dialog.dismiss();

        });
        llWatchAd.setOnClickListener(view1 -> {
            dialog.dismiss();
            AdsManager.showRewardAd(activity, cat_id);

        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
