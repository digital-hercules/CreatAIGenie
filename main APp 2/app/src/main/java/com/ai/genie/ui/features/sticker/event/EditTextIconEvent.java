package com.ai.genie.ui.features.sticker.event;

import android.view.MotionEvent;

import com.ai.genie.ui.features.sticker.StickerView;


public class EditTextIconEvent implements StickerIconEvent {
    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
    }

    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
    }

    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        stickerView.editTextSticker();
    }
}
