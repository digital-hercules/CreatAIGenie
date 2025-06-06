package com.ai.genie.ui.features.featuresfoto;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.ui.features.featuresfoto.draw.BrushColorListener;
import com.ai.genie.ui.features.featuresfoto.draw.ColorAdapter;


public class ColorSplashDialog extends DialogFragment implements BrushColorListener {
    private Bitmap bitmap;
    private ImageView preview;
    private RecyclerView rvColor;

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public static ColorSplashDialog show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2) {
        ColorSplashDialog colorSplashDialog = new ColorSplashDialog();
        colorSplashDialog.setBitmap(bitmap2);
        colorSplashDialog.show(appCompatActivity.getSupportFragmentManager(), "ColorSplashDialog");
        return colorSplashDialog;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ViewCompat.MEASURED_STATE_MASK));
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.color_splash, viewGroup, false);
        this.preview = (ImageView) inflate.findViewById(R.id.preview);
        this.rvColor = (RecyclerView) inflate.findViewById(R.id.rvColorBush);
        this.preview.setImageBitmap(this.bitmap);
        this.rvColor.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.rvColor.setHasFixedSize(true);
        this.rvColor.setAdapter(new ColorAdapter(getContext(), this));
        return inflate;
    }

    public void onColorChanged(String str) {
        Bitmap createBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, paint);
        this.preview.setImageBitmap(createBitmap);
    }
}
