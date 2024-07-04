package com.ai.genie.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ai.genie.R;

public class TypeWriter extends androidx.appcompat.widget.AppCompatTextView{
    public static CharSequence mTexts;
   public static int mIndexs;
    private long mDelay = 10; //Default 500ms delay

    private Activity mContext;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            Log.e("abc","=====mIndexs========"+mIndexs);
            Log.e("abc","=====mTexts========"+mTexts.toString());
            Log.e("abc","=====mTexts.length()========"+mTexts.length());
            if (mIndexs>mTexts.length()){
                return;
            }
            setText(mTexts.subSequence(0, mIndexs++));
            if (mIndexs <= mTexts.length()) {
               /* if (mTexts.toString().equals("Typing...")){
                    NewChatActivity.tvStop.setVisibility(VISIBLE);
                }else {
                    NewChatActivity.tvStop.setVisibility(GONE);
                }
                NewChatActivity.ivLoading.setVisibility(VISIBLE);
                NewChatActivity.ivSend.setVisibility(GONE);
                NewChatActivity.ivCamera.setVisibility(GONE);
                NewChatActivity.ivSpeak.setVisibility(GONE);
                NewChatActivity.ivDocs.setVisibility(View.GONE);*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mContext.isDestroyed()) {
                    return;
                } else {
//                    Glide.with(mContext).load(R.drawable.loading_text).into(NewChatActivity.ivLoading);
                }

//                    Glide.with(mContext).load(R.drawable.loading_text).into(NewChatActivity.ivLoading);
                mHandler.postDelayed(characterAdder, mDelay);
            }
            else{

                Log.e("abc","==========mTexts.toString()===11================="+mTexts.toString());
                if (mTexts.toString().equals("Typing...")){
                    /*NewChatActivity.ivLoading.setVisibility(VISIBLE);
                    NewChatActivity.tvStop.setVisibility(VISIBLE);
*/
                }else {
                   /* NewChatActivity.tvStop.setVisibility(GONE);

                    NewChatActivity.ivLoading.setVisibility(GONE);
                    NewChatActivity.ivSend.setVisibility(View.GONE);
                    NewChatActivity.ivCamera.setVisibility(View.VISIBLE);
                    NewChatActivity.ivSpeak.setVisibility(View.VISIBLE);
                    if (NewChatActivity.type!=2) {
                        if (NewChatActivity.type==0) {
                            NewChatActivity.ivDocs.setVisibility(View.GONE);
                        } else {
                            NewChatActivity.ivDocs.setVisibility(View.VISIBLE);
                        }
                    }*/
                   /* if (NewChatActivity.etText.getText().toString().length() > 1) {
                        NewChatActivity.ivSend.setVisibility(View.VISIBLE);
                        NewChatActivity.ivCamera.setVisibility(View.GONE);
                        NewChatActivity.ivSpeak.setVisibility(View.GONE);
                        NewChatActivity.ivDocs.setVisibility(View.GONE);

                    } else {
                        NewChatActivity.ivSend.setVisibility(View.GONE);
                        NewChatActivity.ivCamera.setVisibility(View.VISIBLE);
                        NewChatActivity.ivSpeak.setVisibility(View.VISIBLE);
                        if (!NewChatActivity.type.equals(getResources().getString(R.string.cat1_sub1))) {
                            if (NewChatActivity.type.equals("0")) {
                                NewChatActivity.ivDocs.setVisibility(View.GONE);
                            } else {
                                NewChatActivity.ivDocs.setVisibility(View.VISIBLE);
                            }
                        }
                    }*/
                }
            }
        }
    };

    public void stop() {
        setText(mTexts.subSequence(0, mIndexs++));
        mHandler.removeCallbacks(characterAdder);

    }

    public void start() {

        setText(mTexts.subSequence(0, mIndexs++));
        mHandler.postDelayed(characterAdder, mDelay);

    }

    public void animateText(Activity context,CharSequence text) {
        mContext =  context;
        mTexts = text;
        mIndexs = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    public void stops() {
        setText(mTexts);
        Log.e("stopage", "stops: " );

            mHandler.removeCallbacks(characterAdder);
        }


}
