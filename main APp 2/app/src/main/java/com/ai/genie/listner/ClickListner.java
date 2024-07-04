package com.ai.genie.listner;

public interface ClickListner {

    void onItemClickCopy(int pos, String answer);

    void onItemClick(int pos, String question);
}
