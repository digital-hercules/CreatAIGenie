package com.ai.genie.ui.features.featuresfoto.crop.adapter;

import com.steelkiwi.cropiwa.AspectRatio;

class AspectRatioCustom extends AspectRatio {
    private int selectedIem;
    private int unselectItem;

    AspectRatioCustom(int i, int i2, int i3, int i4) {
        super(i, i2);
        this.selectedIem = i4;
        this.unselectItem = i3;
    }


    public int getSelectedIem() {
        return this.selectedIem;
    }


    public int getUnselectItem() {
        return this.unselectItem;
    }
}
