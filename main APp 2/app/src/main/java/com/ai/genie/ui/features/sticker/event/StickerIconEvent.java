package com.ai.genie.ui.features.sticker.event;

import android.view.MotionEvent;

import com.ai.genie.ui.features.sticker.StickerView;


public interface StickerIconEvent {
    void onActionDown(StickerView stickerView, MotionEvent motionEvent);

    void onActionMove(StickerView stickerView, MotionEvent motionEvent);

    void onActionUp(StickerView stickerView, MotionEvent motionEvent);
}
