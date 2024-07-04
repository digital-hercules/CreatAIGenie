package com.ai.genie.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.ai.genie.R;
import com.ai.genie.adapter.EditingToolsAdapter;
import com.ai.genie.adapter.PieceToolsAdapter;
import com.ai.genie.adapter.ToolType;
import com.ai.genie.ui.features.featuresfoto.addtext.AddTextProperties;
import com.ai.genie.ui.features.featuresfoto.addtext.TextEditorDialogFragment;
import com.ai.genie.ui.features.featuresfoto.crop.CropDialogFragment;
import com.ai.genie.ui.features.featuresfoto.crop.adapter.AspectRatioPreviewAdapter;
import com.ai.genie.ui.features.featuresfoto.picker.PhotoPicker;
import com.ai.genie.ui.features.featuresfoto.picker.utils.PermissionsUtils;
import com.ai.genie.ui.features.featuresfoto.puzzle.PuzzleLayout;
import com.ai.genie.ui.features.featuresfoto.puzzle.PuzzleLayoutParser;
import com.ai.genie.ui.features.featuresfoto.puzzle.PuzzlePiece;
import com.ai.genie.ui.features.featuresfoto.puzzle.PuzzleView;
import com.ai.genie.ui.features.featuresfoto.puzzle.adapter.PuzzleAdapter;
import com.ai.genie.ui.features.featuresfoto.puzzle.adapter.PuzzleBackgroundAdapter;
import com.ai.genie.ui.features.featuresfoto.puzzle.photopicker.activity.PickImageActivity;
import com.ai.genie.ui.features.featuresfoto.sticker.adapter.RecyclerTabLayout;
import com.ai.genie.ui.features.featuresfoto.sticker.adapter.StickerAdapter;
import com.ai.genie.ui.features.featuresfoto.sticker.adapter.TopTabAdapter;
import com.ai.genie.ui.features.filterscolor.FilterDialogFragment;
import com.ai.genie.ui.features.filterscolor.FilterListener;
import com.ai.genie.ui.features.filterscolor.FilterUtils;
import com.ai.genie.ui.features.filterscolor.FilterViewAdapter;
import com.ai.genie.ui.features.sticker.BitmapStickerIcon;
import com.ai.genie.ui.features.sticker.DrawableSticker;
import com.ai.genie.ui.features.sticker.Sticker;
import com.ai.genie.ui.features.sticker.StickerView;
import com.ai.genie.ui.features.sticker.TextSticker;
import com.ai.genie.ui.features.sticker.event.AlignHorizontallyEvent;
import com.ai.genie.ui.features.sticker.event.DeleteIconEvent;
import com.ai.genie.ui.features.sticker.event.EditTextIconEvent;
import com.ai.genie.ui.features.sticker.event.FlipHorizontallyEvent;
import com.ai.genie.ui.features.sticker.event.ZoomIconEvent;
import com.ai.genie.util.AssetUtils;
import com.ai.genie.util.FileUtils;
import com.ai.genie.util.PuzzleUtils;
import com.ai.genie.util.SharePreferenceUtil;
import com.ai.genie.util.SystemUtil;
import com.ai.genie.util.view.BaseActivity;
import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.steelkiwi.cropiwa.AspectRatio;

import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollageMakerActivity extends BaseActivity implements EditingToolsAdapter.OnItemSelected, AspectRatioPreviewAdapter.OnNewSelectedListener, StickerAdapter.OnClickStickerListener, PuzzleBackgroundAdapter.BackgroundChangeListener, FilterListener, CropDialogFragment.OnCropPhoto, FilterDialogFragment.OnFilterSavePhoto, PieceToolsAdapter.OnPieceFuncItemSelected, PuzzleAdapter.OnItemClickListener {
    public static CollageMakerActivity puzzle;
    private static CollageMakerActivity instance;
    public ImageView addNewSticker;

    public ImageView addNewText;

    public ConstraintLayout changeBackgroundLayout;
    public ConstraintLayout changeLayoutLayout;
    public AspectRatio currentAspect;
    public PuzzleBackgroundAdapter.SquareView currentBackgroundState;
    public ToolType currentMode;
    public int deviceWidth = 0;
    public ConstraintLayout filterLayout;
    public List<Bitmap> lstBitmapWithFilter = new ArrayList();
    public List<Drawable> lstOriginalDrawable = new ArrayList();
    public List<String> lstPaths;
    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String str, Object obj) {
            try {
                return BitmapFactory.decodeStream(CollageMakerActivity.this.getAssets().open(str));
            } catch (IOException unused) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object obj) {
            bitmap.recycle();
        }
    };
    public RecyclerView mRvTools;
    public float pieceBorderRadius;
    public float piecePadding;
    public PuzzleLayout puzzleLayout;
    public PuzzleView puzzleView;
    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            switch (seekBar.getId()) {
                case R.id.sk_border:
                    CollageMakerActivity.this.puzzleView.setPiecePadding((float) i);
                    break;
                case R.id.sk_border_radius:
                    CollageMakerActivity.this.puzzleView.setPieceRadian((float) i);
                    break;
            }
            CollageMakerActivity.this.puzzleView.invalidate();
        }
    };
    public RecyclerView rvFilterView;
    public RecyclerView rvPieceControl;
    public SeekBar stickerAlpha;
    public ConstraintLayout stickerLayout;
    public List<Target> targets = new ArrayList();
    public TextEditorDialogFragment.TextEditor textEditor;
    public TextEditorDialogFragment textEditorDialogFragment;
    public ConstraintLayout textLayout;
    public LinearLayout wrapStickerList;
    StickerView.OnStickerOperationListener onStickerOperationListener = new StickerView.OnStickerOperationListener() {
        public void onStickerDragFinished(@NonNull Sticker sticker) {
        }

        public void onStickerFlipped(@NonNull Sticker sticker) {
        }

        public void onStickerTouchedDown(@NonNull Sticker sticker) {
        }

        public void onStickerZoomFinished(@NonNull Sticker sticker) {
        }

        public void onTouchDownForBeauty(float f, float f2) {
        }

        public void onTouchDragForBeauty(float f, float f2) {
        }

        public void onTouchUpForBeauty(float f, float f2) {
        }

        public void onStickerAdded(@NonNull Sticker sticker) {
            CollageMakerActivity.this.stickerAlpha.setVisibility(View.VISIBLE);
            CollageMakerActivity.this.stickerAlpha.setProgress(sticker.getAlpha());
        }

        public void onStickerClicked(@NonNull Sticker sticker) {
            CollageMakerActivity.this.stickerAlpha.setVisibility(View.VISIBLE);
            CollageMakerActivity.this.stickerAlpha.setProgress(sticker.getAlpha());
        }

        public void onStickerDeleted(@NonNull Sticker sticker) {
            CollageMakerActivity.this.stickerAlpha.setVisibility(View.GONE);
        }

        public void onStickerTouchOutside() {
            CollageMakerActivity.this.stickerAlpha.setVisibility(View.GONE);
        }

        public void onStickerDoubleTapped(@NonNull Sticker sticker) {
            if (sticker instanceof TextSticker) {
                sticker.setShow(false);
                CollageMakerActivity.this.puzzleView.setHandlingSticker((Sticker) null);
                TextEditorDialogFragment unused = CollageMakerActivity.this.textEditorDialogFragment = TextEditorDialogFragment.show(CollageMakerActivity.this, ((TextSticker) sticker).getAddTextProperties());
                TextEditorDialogFragment.TextEditor unused2 = CollageMakerActivity.this.textEditor = new TextEditorDialogFragment.TextEditor() {
                    public void onDone(AddTextProperties addTextProperties) {
                        CollageMakerActivity.this.puzzleView.getStickers().remove(CollageMakerActivity.this.puzzleView.getLastHandlingSticker());
                        CollageMakerActivity.this.puzzleView.addSticker(new TextSticker(CollageMakerActivity.this, addTextProperties));
                    }

                    public void onBackButton() {
                        CollageMakerActivity.this.puzzleView.showLastHandlingSticker();
                    }
                };
                CollageMakerActivity.this.textEditorDialogFragment.setOnTextEditorListener(CollageMakerActivity.this.textEditor);
            }
        }
    };
    private LinearLayout changeBorder;
    private int deviceHeight = 0;
    private KeyboardHeightProvider keyboardHeightProvider;
    private RelativeLayout loadingView;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this, true);
    private ConstraintLayout mainActivity;
    private PieceToolsAdapter pieceToolsAdapter = new PieceToolsAdapter(this);
    private RecyclerView puzzleList;
    private RecyclerView radiusLayout;
    private RecyclerView rvBackgroundBlur;
    private RecyclerView rvBackgroundColor;
    private RecyclerView rvBackgroundGradient;
    private Button saveBitmap;
    private RelativeLayout saveControl;
    private SeekBar sbChangeBorderRadius;
    private SeekBar sbChangeBorderSize;
    private TextView tvChangeBackgroundBlur;
    private TextView tvChangeBackgroundColor;
    private TextView tvChangeBackgroundGradient;
    private TextView tvChangeBorder;
    private TextView tvChangeLayout;
    private TextView tvChangeRatio;
    private ConstraintLayout wrapPuzzleView;
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgCloseBackground:
                case R.id.imgCloseFilter:
                case R.id.imgCloseLayout:
                case R.id.imgCloseSticker:
                case R.id.imgCloseText:
                    CollageMakerActivity.this.onBackPressed();
                    return;
                case R.id.imgSaveBackground:
                    CollageMakerActivity.this.slideDown(CollageMakerActivity.this.changeBackgroundLayout);
                    CollageMakerActivity.this.slideUp(CollageMakerActivity.this.mRvTools);
                    CollageMakerActivity.this.slideDownSaveView();
                    CollageMakerActivity.this.showDownFunction();
                    CollageMakerActivity.this.puzzleView.setLocked(true);
                    CollageMakerActivity.this.puzzleView.setTouchEnable(true);
                    if (CollageMakerActivity.this.puzzleView.getBackgroundResourceMode() == 0) {
                        CollageMakerActivity.this.currentBackgroundState.isColor = true;
                        CollageMakerActivity.this.currentBackgroundState.isBitmap = false;
                        CollageMakerActivity.this.currentBackgroundState.drawableId = ((ColorDrawable) CollageMakerActivity.this.puzzleView.getBackground()).getColor();
                        CollageMakerActivity.this.currentBackgroundState.drawable = null;
                    } else if (CollageMakerActivity.this.puzzleView.getBackgroundResourceMode() == 1) {
                        CollageMakerActivity.this.currentBackgroundState.isColor = false;
                        CollageMakerActivity.this.currentBackgroundState.isBitmap = false;
                        CollageMakerActivity.this.currentBackgroundState.drawable = CollageMakerActivity.this.puzzleView.getBackground();
                    } else {
                        CollageMakerActivity.this.currentBackgroundState.isColor = false;
                        CollageMakerActivity.this.currentBackgroundState.isBitmap = true;
                        CollageMakerActivity.this.currentBackgroundState.drawable = CollageMakerActivity.this.puzzleView.getBackground();
                    }
                    ToolType unused = CollageMakerActivity.this.currentMode = ToolType.NONE;
                    return;
                case R.id.imgSaveFilter:
                    CollageMakerActivity.this.slideDown(CollageMakerActivity.this.filterLayout);
                    CollageMakerActivity.this.slideUp(CollageMakerActivity.this.mRvTools);
                    ToolType unused2 = CollageMakerActivity.this.currentMode = ToolType.NONE;
                    CollageMakerActivity.this.slideDownSaveView();
                    return;
                case R.id.imgSaveLayout:
                    CollageMakerActivity.this.slideUp(CollageMakerActivity.this.mRvTools);
                    CollageMakerActivity.this.slideDown(CollageMakerActivity.this.changeLayoutLayout);
                    CollageMakerActivity.this.slideDownSaveView();
                    CollageMakerActivity.this.showDownFunction();
                    PuzzleLayout unused3 = CollageMakerActivity.this.puzzleLayout = CollageMakerActivity.this.puzzleView.getPuzzleLayout();
                    float unused4 = CollageMakerActivity.this.pieceBorderRadius = CollageMakerActivity.this.puzzleView.getPieceRadian();
                    float unused5 = CollageMakerActivity.this.piecePadding = CollageMakerActivity.this.puzzleView.getPiecePadding();
                    CollageMakerActivity.this.puzzleView.setLocked(true);
                    CollageMakerActivity.this.puzzleView.setTouchEnable(true);
                    AspectRatio unused6 = CollageMakerActivity.this.currentAspect = CollageMakerActivity.this.puzzleView.getAspectRatio();
                    ToolType unused7 = CollageMakerActivity.this.currentMode = ToolType.NONE;
                    return;
                case R.id.imgSaveSticker:
                    CollageMakerActivity.this.puzzleView.setHandlingSticker((Sticker) null);
                    CollageMakerActivity.this.stickerAlpha.setVisibility(View.GONE);
                    CollageMakerActivity.this.addNewSticker.setVisibility(View.GONE);
                    CollageMakerActivity.this.slideUp(CollageMakerActivity.this.wrapStickerList);
                    CollageMakerActivity.this.slideDown(CollageMakerActivity.this.stickerLayout);
                    CollageMakerActivity.this.slideUp(CollageMakerActivity.this.mRvTools);
                    CollageMakerActivity.this.slideDownSaveView();
                    CollageMakerActivity.this.puzzleView.setLocked(true);
                    CollageMakerActivity.this.puzzleView.setTouchEnable(true);
                    ToolType unused8 = CollageMakerActivity.this.currentMode = ToolType.NONE;
                    return;
                case R.id.imgSaveText:
                    CollageMakerActivity.this.puzzleView.setHandlingSticker((Sticker) null);
                    CollageMakerActivity.this.puzzleView.setLocked(true);
                    CollageMakerActivity.this.addNewText.setVisibility(View.GONE);
                    CollageMakerActivity.this.slideDown(CollageMakerActivity.this.textLayout);
                    CollageMakerActivity.this.slideUp(CollageMakerActivity.this.mRvTools);
                    CollageMakerActivity.this.slideDownSaveView();
                    CollageMakerActivity.this.puzzleView.setLocked(true);
                    CollageMakerActivity.this.puzzleView.setTouchEnable(true);
                    ToolType unused9 = CollageMakerActivity.this.currentMode = ToolType.NONE;
                    return;
                case R.id.tv_blur:
                    CollageMakerActivity.this.selectBackgroundBlur();
                    return;
                case R.id.tv_change_border:
                    CollageMakerActivity.this.selectBorderTool();
                    return;
                case R.id.tv_change_layout:
                    CollageMakerActivity.this.selectLayoutTool();
                    return;
                case R.id.tv_change_ratio:
                    CollageMakerActivity.this.selectRadiusTool();
                    return;
                case R.id.tv_color:
                    CollageMakerActivity.this.selectBackgroundColorTab();
                    return;
                case R.id.tv_radian:
                    CollageMakerActivity.this.selectBackgroundGradientTab();
                    return;
                default:
                    return;
            }
        }
    };

    public static CollageMakerActivity getInstance() {
        return instance;
    }

    public static void lambda$onCreate$0(CollageMakerActivity collageMakerActivity) {
        collageMakerActivity.slideDown(collageMakerActivity.rvPieceControl);
        collageMakerActivity.slideUp(collageMakerActivity.mRvTools);
        collageMakerActivity.slideDownSaveView();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) collageMakerActivity.rvPieceControl.getLayoutParams();
        layoutParams.bottomMargin = 0;
        collageMakerActivity.rvPieceControl.setLayoutParams(layoutParams);
        collageMakerActivity.currentMode = ToolType.NONE;
    }

    public static void lambda$onCreate$2(CollageMakerActivity collageMakerActivity, View view) {
        collageMakerActivity.puzzleView.setHandlingSticker((Sticker) null);
        collageMakerActivity.openTextFragment();
    }

    public static void lambda$onCreate$4(CollageMakerActivity collageMakerActivity, View view) {
        collageMakerActivity.addNewSticker.setVisibility(View.GONE);
        collageMakerActivity.slideUp(collageMakerActivity.wrapStickerList);
    }

    public static void lambda$onCreate$5(CollageMakerActivity collageMakerActivity) {
        collageMakerActivity.slideDown(collageMakerActivity.changeLayoutLayout);
        collageMakerActivity.slideDown(collageMakerActivity.stickerLayout);
        collageMakerActivity.slideDown(collageMakerActivity.textLayout);
        collageMakerActivity.slideDown(collageMakerActivity.changeBackgroundLayout);
        collageMakerActivity.slideDown(collageMakerActivity.filterLayout);
        collageMakerActivity.slideDown(collageMakerActivity.rvPieceControl);
    }

    public static void lambda$onCreate$6(CollageMakerActivity collageMakerActivity) {
        collageMakerActivity.changeLayoutLayout.setAlpha(1.0f);
        collageMakerActivity.stickerLayout.setAlpha(1.0f);
        collageMakerActivity.textLayout.setAlpha(1.0f);
        collageMakerActivity.filterLayout.setAlpha(1.0f);
        collageMakerActivity.changeBackgroundLayout.setAlpha(1.0f);
        collageMakerActivity.rvPieceControl.setAlpha(1.0f);
    }

    public static void lambda$onCreate$7(CollageMakerActivity collageMakerActivity, int i) {
        if (i <= 0) {
            SharePreferenceUtil.setHeightOfNotch(collageMakerActivity.getApplicationContext(), -i);
        } else if (collageMakerActivity.textEditorDialogFragment != null) {
            collageMakerActivity.textEditorDialogFragment.updateAddTextBottomToolbarHeight(SharePreferenceUtil.getHeightOfNotch(collageMakerActivity.getApplicationContext()) + i);
            SharePreferenceUtil.setHeightOfKeyboard(collageMakerActivity.getApplicationContext(), i + SharePreferenceUtil.getHeightOfNotch(collageMakerActivity.getApplicationContext()));
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.puzzle_layout);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        this.deviceWidth = getResources().getDisplayMetrics().widthPixels;
        this.deviceHeight = getResources().getDisplayMetrics().heightPixels;
        this.loadingView = (RelativeLayout) findViewById(R.id.loadingView);
        this.puzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
        this.wrapPuzzleView = (ConstraintLayout) findViewById(R.id.wrapPuzzleView);
        this.filterLayout = (ConstraintLayout) findViewById(R.id.filterLayout);
        this.rvFilterView = (RecyclerView) findViewById(R.id.rvFilterView);
        this.mRvTools = (RecyclerView) findViewById(R.id.rvConstraintTools);
        this.mRvTools.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.mRvTools.setAdapter(this.mEditingToolsAdapter);
        this.rvPieceControl = (RecyclerView) findViewById(R.id.rvPieceControl);
        this.rvPieceControl.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.rvPieceControl.setAdapter(this.pieceToolsAdapter);
        this.sbChangeBorderSize = (SeekBar) findViewById(R.id.sk_border);
        this.sbChangeBorderSize.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.sbChangeBorderRadius = (SeekBar) findViewById(R.id.sk_border_radius);
        this.sbChangeBorderRadius.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.lstPaths = getIntent().getStringArrayListExtra(PickImageActivity.KEY_DATA_RESULT);
        this.puzzleLayout = PuzzleUtils.getPuzzleLayouts(this.lstPaths.size()).get(0);
        this.puzzleView.setPuzzleLayout(this.puzzleLayout);
        this.puzzleView.setTouchEnable(true);
        this.puzzleView.setNeedDrawLine(false);
        this.puzzleView.setNeedDrawOuterLine(false);
        this.puzzleView.setLineSize(4);
        this.puzzleView.setPiecePadding(6.0f);
        this.puzzleView.setPieceRadian(15.0f);
        this.puzzleView.setLineColor(getResources().getColor(R.color.white));
        this.puzzleView.setSelectedLineColor(getResources().getColor(R.color.colorAccent));
        this.puzzleView.setHandleBarColor(getResources().getColor(R.color.colorAccent));
        this.puzzleView.setAnimateDuration(300);
        this.puzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
            public void onPieceSelected(PuzzlePiece puzzlePiece, int i) {
                CollageMakerActivity.this.slideDown(CollageMakerActivity.this.mRvTools);
                CollageMakerActivity.this.slideUp(CollageMakerActivity.this.rvPieceControl);
                CollageMakerActivity.this.slideUpSaveView();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) CollageMakerActivity.this.rvPieceControl.getLayoutParams();
                layoutParams.bottomMargin = SystemUtil.dpToPx(CollageMakerActivity.this.getApplicationContext(), 10);
                CollageMakerActivity.this.rvPieceControl.setLayoutParams(layoutParams);
                ToolType unused = CollageMakerActivity.this.currentMode = ToolType.PIECE;
            }
        });
        this.puzzleView.setOnPieceUnSelectedListener(new PuzzleView.OnPieceUnSelectedListener() {
            public final void onPieceUnSelected() {
                CollageMakerActivity.lambda$onCreate$0(CollageMakerActivity.this);
            }
        });
        this.saveControl = (RelativeLayout) findViewById(R.id.saveControl);
        this.puzzleView.post(new Runnable() {
            public final void run() {
                CollageMakerActivity.this.loadPhoto();
            }
        });
        findViewById(R.id.exitEditMode).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CollageMakerActivity.this.finish();

            }
        });
        findViewById(R.id.imgCloseLayout).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgSaveLayout).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgCloseSticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgCloseFilter).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgCloseBackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgSaveSticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgCloseText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgSaveText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgSaveFilter).setOnClickListener(this.onClickListener);
        findViewById(R.id.imgSaveBackground).setOnClickListener(this.onClickListener);
        this.changeLayoutLayout = (ConstraintLayout) findViewById(R.id.changeLayoutLayout);
        this.changeBorder = (LinearLayout) findViewById(R.id.change_border);
        this.tvChangeLayout = (TextView) findViewById(R.id.tv_change_layout);
        this.tvChangeLayout.setOnClickListener(this.onClickListener);
        this.tvChangeBorder = (TextView) findViewById(R.id.tv_change_border);
        this.tvChangeBorder.setOnClickListener(this.onClickListener);
        this.tvChangeRatio = (TextView) findViewById(R.id.tv_change_ratio);
        this.tvChangeRatio.setOnClickListener(this.onClickListener);
        this.tvChangeBackgroundColor = (TextView) findViewById(R.id.tv_color);
        this.tvChangeBackgroundColor.setOnClickListener(this.onClickListener);
        this.tvChangeBackgroundGradient = (TextView) findViewById(R.id.tv_radian);
        this.tvChangeBackgroundGradient.setOnClickListener(this.onClickListener);
        this.tvChangeBackgroundBlur = (TextView) findViewById(R.id.tv_blur);
        this.tvChangeBackgroundBlur.setOnClickListener(this.onClickListener);
        PuzzleAdapter puzzleAdapter = new PuzzleAdapter();
        this.puzzleList = (RecyclerView) findViewById(R.id.puzzleList);
        this.puzzleList.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.puzzleList.setAdapter(puzzleAdapter);
        puzzleAdapter.refreshData(PuzzleUtils.getPuzzleLayouts(this.lstPaths.size()), (List<Bitmap>) null);
        puzzleAdapter.setOnItemClickListener(this);
        AspectRatioPreviewAdapter aspectRatioPreviewAdapter = new AspectRatioPreviewAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        this.radiusLayout = (RecyclerView) findViewById(R.id.radioLayout);
        this.radiusLayout.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.radiusLayout.setAdapter(aspectRatioPreviewAdapter);
        this.textLayout = (ConstraintLayout) findViewById(R.id.textControl);
        this.addNewText = (ImageView) findViewById(R.id.addNewText);
        this.addNewText.setVisibility(View.GONE);
        this.addNewText.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CollageMakerActivity.lambda$onCreate$2(CollageMakerActivity.this, view);
            }
        });
        this.wrapStickerList = (LinearLayout) findViewById(R.id.wrapStickerList);
        ViewPager viewPager = (ViewPager) findViewById(R.id.sticker_viewpaper);
        this.stickerLayout = (ConstraintLayout) findViewById(R.id.stickerLayout);
        this.stickerAlpha = (SeekBar) findViewById(R.id.stickerAlpha);
        this.stickerAlpha.setVisibility(View.GONE);
        this.stickerAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = CollageMakerActivity.this.puzzleView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        this.saveBitmap = (Button) findViewById(R.id.save);
        this.saveBitmap.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {


                if (PermissionsUtils.checkWriteStoragePermission((Activity) CollageMakerActivity.this)) {
                    Bitmap createBitmap = FileUtils.createBitmap(CollageMakerActivity.this.puzzleView, 1920);
                    Bitmap createBitmap2 = CollageMakerActivity.this.puzzleView.createBitmap();
                    new SavePuzzleAsFile().execute(new Bitmap[]{createBitmap, createBitmap2});
                }
            }
        });
        this.addNewSticker = (ImageView) findViewById(R.id.addNewSticker);
        this.addNewSticker.setVisibility(View.GONE);
        this.addNewSticker.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CollageMakerActivity.lambda$onCreate$4(CollageMakerActivity.this, view);
            }
        });
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_white_18dp), 0, BitmapStickerIcon.REMOVE);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_scale_white_18dp), 3, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_flip_white_18dp), 1, BitmapStickerIcon.FLIP);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon bitmapStickerIcon4 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_rotate), 3, BitmapStickerIcon.ROTATE);
        bitmapStickerIcon4.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon5 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_edit), 1, BitmapStickerIcon.EDIT);
        bitmapStickerIcon5.setIconEvent(new EditTextIconEvent());
        BitmapStickerIcon bitmapStickerIcon6 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_center), 2, BitmapStickerIcon.ALIGN_HORIZONTALLY);
        bitmapStickerIcon6.setIconEvent(new AlignHorizontallyEvent());
        this.puzzleView.setIcons(Arrays.asList(new BitmapStickerIcon[]{bitmapStickerIcon, bitmapStickerIcon2, bitmapStickerIcon3, bitmapStickerIcon5, bitmapStickerIcon4, bitmapStickerIcon6}));
        this.puzzleView.setConstrained(true);
        this.puzzleView.setOnStickerOperationListener(this.onStickerOperationListener);
        viewPager.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 13;
            }

            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view.equals(obj);
            }

            public void destroyItem(@NonNull View view, int i, @NonNull Object obj) {
                ((ViewPager) view).removeView((View) obj);
            }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(CollageMakerActivity.this.getBaseContext()).inflate(R.layout.sticker_items, (ViewGroup) null, false);
                RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(CollageMakerActivity.this.getApplicationContext(), 4));
                switch (i) {
                    case 0:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstHeardes(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 1:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstEmoj(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 2:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstTexts(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 3:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstOthers(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 4:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstGiddy(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 5:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstGlasses(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 6:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstTies(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 7:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstCatFaces(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 8:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstCkeeks(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 9:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstDiadems(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 10:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstEyes(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 11:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstMuscle(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                    case 12:
                        recyclerView.setAdapter(new StickerAdapter(CollageMakerActivity.this.getApplicationContext(), AssetUtils.lstTatoos(), CollageMakerActivity.this.deviceWidth, CollageMakerActivity.this));
                        break;
                }
                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recyclerTabLayout = (RecyclerTabLayout) findViewById(R.id.recycler_tab_layout);
        recyclerTabLayout.setUpWithAdapter(new TopTabAdapter(viewPager, getApplicationContext()));
        recyclerTabLayout.setPositionThreshold(0.5f);
        recyclerTabLayout.setBackgroundColor(getResources().getColor(R.color.basic_white));
        this.changeBackgroundLayout = (ConstraintLayout) findViewById(R.id.changeBackgroundLayout);
        this.mainActivity = (ConstraintLayout) findViewById(R.id.puzzle_layout);
        this.changeLayoutLayout.setAlpha(0.0f);
        this.stickerLayout.setAlpha(0.0f);
        this.textLayout.setAlpha(0.0f);
        this.filterLayout.setAlpha(0.0f);
        this.changeBackgroundLayout.setAlpha(0.0f);
        this.rvPieceControl.setAlpha(0.0f);
        this.mainActivity.post(new Runnable() {
            public final void run() {
                CollageMakerActivity.lambda$onCreate$5(CollageMakerActivity.this);
            }
        });
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                CollageMakerActivity.lambda$onCreate$6(CollageMakerActivity.this);
            }
        }, 1000);
        SharePreferenceUtil.setHeightOfKeyboard(getApplicationContext(), 0);
        this.keyboardHeightProvider = new KeyboardHeightProvider(this);
        this.keyboardHeightProvider.addKeyboardListener(new KeyboardHeightProvider.KeyboardListener() {
            public final void onHeightChanged(int i) {
                CollageMakerActivity.lambda$onCreate$7(CollageMakerActivity.this, i);
            }
        });
        showLoading(false);
        this.currentBackgroundState = new PuzzleBackgroundAdapter.SquareView(Color.parseColor("#ffffff"), "", true);
        this.rvBackgroundColor = (RecyclerView) findViewById(R.id.colorList);
        this.rvBackgroundColor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.rvBackgroundColor.setHasFixedSize(true);
        this.rvBackgroundColor.setAdapter(new PuzzleBackgroundAdapter(getApplicationContext(), this));
        this.rvBackgroundGradient = (RecyclerView) findViewById(R.id.radianList);
        this.rvBackgroundGradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.rvBackgroundGradient.setHasFixedSize(true);
        this.rvBackgroundGradient.setAdapter(new PuzzleBackgroundAdapter(getApplicationContext(), (PuzzleBackgroundAdapter.BackgroundChangeListener) this, true));
        this.rvBackgroundBlur = (RecyclerView) findViewById(R.id.backgroundList);
        this.rvBackgroundBlur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.rvBackgroundBlur.setHasFixedSize(true);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.puzzleView.getLayoutParams();
        layoutParams.height = point.x;
        layoutParams.width = point.x;
        this.puzzleView.setLayoutParams(layoutParams);
        this.currentAspect = new AspectRatio(1, 1);
        this.puzzleView.setAspectRatio(new AspectRatio(1, 1));
        puzzle = this;
        this.currentMode = ToolType.NONE;
        CGENativeLibrary.setLoadImageCallback(this.mLoadImageCallback, (Object) null);
        instance = this;
    }

    public void lambda$onCreate$3(CollageMakerActivity collageMakerActivity, View view) {
        if (PermissionsUtils.checkWriteStoragePermission((Activity) collageMakerActivity)) {
            Bitmap createBitmap = FileUtils.createBitmap(collageMakerActivity.puzzleView, 1920);
            Bitmap createBitmap2 = collageMakerActivity.puzzleView.createBitmap();
            new SavePuzzleAsFile().execute(new Bitmap[]{createBitmap, createBitmap2});
        }
    }

    private void openTextFragment() {
        this.textEditorDialogFragment = TextEditorDialogFragment.show(this);
        this.textEditor = new TextEditorDialogFragment.TextEditor() {
            public void onDone(AddTextProperties addTextProperties) {
                CollageMakerActivity.this.puzzleView.addSticker(new TextSticker(CollageMakerActivity.this.getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (CollageMakerActivity.this.puzzleView.getStickers().isEmpty()) {
                    CollageMakerActivity.this.onBackPressed();
                }
            }
        };
        this.textEditorDialogFragment.setOnTextEditorListener(this.textEditor);
    }

    public void isPermissionGranted(boolean z, String str) {
        if (z) {
            Bitmap createBitmap = FileUtils.createBitmap(this.puzzleView, 1920);
            Bitmap createBitmap2 = this.puzzleView.createBitmap();
            new SavePuzzleAsFile().execute(new Bitmap[]{createBitmap, createBitmap2});
        }
    }

    public void showLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            this.loadingView.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        this.loadingView.setVisibility(View.GONE);
    }

    public void selectBackgroundBlur() {
        ArrayList arrayList = new ArrayList();
        for (PuzzlePiece drawable : this.puzzleView.getPuzzlePieces()) {
            arrayList.add(drawable.getDrawable());
        }
        PuzzleBackgroundAdapter puzzleBackgroundAdapter = new PuzzleBackgroundAdapter(getApplicationContext(), (PuzzleBackgroundAdapter.BackgroundChangeListener) this, (List<Drawable>) arrayList);
        puzzleBackgroundAdapter.setSelectedSquareIndex(-1);
        this.rvBackgroundBlur.setAdapter(puzzleBackgroundAdapter);
        this.rvBackgroundBlur.setVisibility(View.VISIBLE);
        this.tvChangeBackgroundBlur.setBackgroundResource(R.drawable.border_bottom);
        this.tvChangeBackgroundBlur.setTextColor(getResources().getColor(R.color.colorAccent));
        this.rvBackgroundGradient.setVisibility(View.GONE);
        this.tvChangeBackgroundGradient.setBackgroundResource(0);
        this.tvChangeBackgroundGradient.setTextColor(getResources().getColor(R.color.white));
        this.rvBackgroundColor.setVisibility(View.GONE);
        this.tvChangeBackgroundColor.setBackgroundResource(0);
        this.tvChangeBackgroundColor.setTextColor(getResources().getColor(R.color.white));
    }

    public void selectBackgroundColorTab() {
        this.rvBackgroundColor.setVisibility(View.VISIBLE);
        this.tvChangeBackgroundColor.setBackgroundResource(R.drawable.border_bottom);
        this.tvChangeBackgroundColor.setTextColor(getResources().getColor(R.color.colorAccent));
        this.rvBackgroundColor.scrollToPosition(0);
        ((PuzzleBackgroundAdapter) this.rvBackgroundColor.getAdapter()).setSelectedSquareIndex(-1);
        this.rvBackgroundColor.getAdapter().notifyDataSetChanged();
        this.rvBackgroundGradient.setVisibility(View.GONE);
        this.tvChangeBackgroundGradient.setBackgroundResource(0);
        this.tvChangeBackgroundGradient.setTextColor(getResources().getColor(R.color.white));
        this.rvBackgroundBlur.setVisibility(View.GONE);
        this.tvChangeBackgroundBlur.setBackgroundResource(0);
        this.tvChangeBackgroundBlur.setTextColor(getResources().getColor(R.color.white));
    }

    public void selectBackgroundGradientTab() {
        this.rvBackgroundGradient.setVisibility(View.VISIBLE);
        this.tvChangeBackgroundGradient.setBackgroundResource(R.drawable.border_bottom);
        this.tvChangeBackgroundGradient.setTextColor(getResources().getColor(R.color.colorAccent));
        this.rvBackgroundGradient.scrollToPosition(0);
        ((PuzzleBackgroundAdapter) this.rvBackgroundGradient.getAdapter()).setSelectedSquareIndex(-1);
        this.rvBackgroundGradient.getAdapter().notifyDataSetChanged();
        this.rvBackgroundColor.setVisibility(View.GONE);
        this.tvChangeBackgroundColor.setBackgroundResource(0);
        this.tvChangeBackgroundColor.setTextColor(getResources().getColor(R.color.white));
        this.rvBackgroundBlur.setVisibility(View.GONE);
        this.tvChangeBackgroundBlur.setBackgroundResource(0);
        this.tvChangeBackgroundBlur.setTextColor(getResources().getColor(R.color.white));
    }


    public void selectLayoutTool() {
        this.puzzleList.setVisibility(View.VISIBLE);
        this.tvChangeLayout.setBackgroundResource(R.drawable.border_bottom);
        this.tvChangeLayout.setTextColor(getResources().getColor(R.color.colorAccent));
        this.changeBorder.setVisibility(View.GONE);
        this.tvChangeBorder.setBackgroundResource(0);
        this.tvChangeBorder.setTextColor(getResources().getColor(R.color.white));
        this.radiusLayout.setVisibility(View.GONE);
        this.tvChangeRatio.setBackgroundResource(0);
        this.tvChangeRatio.setTextColor(getResources().getColor(R.color.white));
    }


    public void selectRadiusTool() {
        this.radiusLayout.setVisibility(View.VISIBLE);
        this.tvChangeRatio.setTextColor(getResources().getColor(R.color.colorAccent));
        this.tvChangeRatio.setBackgroundResource(R.drawable.border_bottom);
        this.puzzleList.setVisibility(View.GONE);
        this.tvChangeLayout.setBackgroundResource(0);
        this.tvChangeLayout.setTextColor(getResources().getColor(R.color.white));
        this.changeBorder.setVisibility(View.GONE);
        this.tvChangeBorder.setBackgroundResource(0);
        this.tvChangeBorder.setTextColor(getResources().getColor(R.color.white));
    }


    public void selectBorderTool() {
        this.changeBorder.setVisibility(View.VISIBLE);
        this.tvChangeBorder.setBackgroundResource(R.drawable.border_bottom);
        this.tvChangeBorder.setTextColor(getResources().getColor(R.color.colorAccent));
        this.puzzleList.setVisibility(View.GONE);
        this.tvChangeLayout.setBackgroundResource(0);
        this.tvChangeLayout.setTextColor(getResources().getColor(R.color.white));
        this.radiusLayout.setVisibility(View.GONE);
        this.tvChangeRatio.setBackgroundResource(0);
        this.tvChangeRatio.setTextColor(getResources().getColor(R.color.white));
        this.sbChangeBorderRadius.setProgress((int) this.puzzleView.getPieceRadian());
        this.sbChangeBorderSize.setProgress((int) this.puzzleView.getPiecePadding());
    }

    private void showUpFunction(View view) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mainActivity);
        constraintSet.connect(this.wrapPuzzleView.getId(), 3, 0, 3, 0);
        constraintSet.connect(this.wrapPuzzleView.getId(), 1, this.mainActivity.getId(), 1, 0);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.wrapPuzzleView.getId(), 4, view.getId(), 3, 0);
        constraintSet2.connect(this.wrapPuzzleView.getId(), 2, this.mainActivity.getId(), 2, 0);
        constraintSet.applyTo(this.mainActivity);
    }


    public void showDownFunction() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mainActivity);
        constraintSet.connect(this.wrapPuzzleView.getId(), 3, 0, 3, 0);
        constraintSet.connect(this.wrapPuzzleView.getId(), 1, this.mainActivity.getId(), 1, 0);
        constraintSet.connect(this.wrapPuzzleView.getId(), 4, this.mRvTools.getId(), 3, 0);
        constraintSet.connect(this.wrapPuzzleView.getId(), 2, this.mainActivity.getId(), 2, 0);
        constraintSet.applyTo(this.mainActivity);
    }

    public void onToolSelected(ToolType toolType) {
        this.currentMode = toolType;
        switch (toolType) {
            case LAYOUT:
                this.puzzleLayout = this.puzzleView.getPuzzleLayout();
                this.currentAspect = this.puzzleView.getAspectRatio();
                this.pieceBorderRadius = this.puzzleView.getPieceRadian();
                this.piecePadding = this.puzzleView.getPiecePadding();
                this.puzzleList.scrollToPosition(0);
                ((PuzzleAdapter) this.puzzleList.getAdapter()).setSelectedIndex(-1);
                this.puzzleList.getAdapter().notifyDataSetChanged();
                this.radiusLayout.scrollToPosition(0);
                ((AspectRatioPreviewAdapter) this.radiusLayout.getAdapter()).setLastSelectedView(-1);
                this.radiusLayout.getAdapter().notifyDataSetChanged();
                selectLayoutTool();
                slideUpSaveView();
                slideUp(this.changeLayoutLayout);
                slideDown(this.mRvTools);
                showUpFunction(this.changeLayoutLayout);
                this.puzzleView.setLocked(false);
                this.puzzleView.setTouchEnable(false);
                return;
            case BORDER:
                this.puzzleLayout = this.puzzleView.getPuzzleLayout();
                this.currentAspect = this.puzzleView.getAspectRatio();
                this.pieceBorderRadius = this.puzzleView.getPieceRadian();
                this.piecePadding = this.puzzleView.getPiecePadding();
                this.puzzleList.scrollToPosition(0);
                ((PuzzleAdapter) this.puzzleList.getAdapter()).setSelectedIndex(-1);
                this.puzzleList.getAdapter().notifyDataSetChanged();
                this.radiusLayout.scrollToPosition(0);
                ((AspectRatioPreviewAdapter) this.radiusLayout.getAdapter()).setLastSelectedView(-1);
                this.radiusLayout.getAdapter().notifyDataSetChanged();
                selectBorderTool();
                slideUpSaveView();
                slideUp(this.changeLayoutLayout);
                slideDown(this.mRvTools);
                showUpFunction(this.changeLayoutLayout);
                this.puzzleView.setLocked(false);
                this.puzzleView.setTouchEnable(false);
                return;
            case RATIO:
                this.puzzleLayout = this.puzzleView.getPuzzleLayout();
                this.currentAspect = this.puzzleView.getAspectRatio();
                this.pieceBorderRadius = this.puzzleView.getPieceRadian();
                this.piecePadding = this.puzzleView.getPiecePadding();
                this.puzzleList.scrollToPosition(0);
                ((PuzzleAdapter) this.puzzleList.getAdapter()).setSelectedIndex(-1);
                this.puzzleList.getAdapter().notifyDataSetChanged();
                this.radiusLayout.scrollToPosition(0);
                ((AspectRatioPreviewAdapter) this.radiusLayout.getAdapter()).setLastSelectedView(-1);
                this.radiusLayout.getAdapter().notifyDataSetChanged();
                selectRadiusTool();
                slideUpSaveView();
                slideUp(this.changeLayoutLayout);
                slideDown(this.mRvTools);
                showUpFunction(this.changeLayoutLayout);
                this.puzzleView.setLocked(false);
                this.puzzleView.setTouchEnable(false);
                return;
            case FILTER:
                if (this.lstOriginalDrawable.isEmpty()) {
                    for (PuzzlePiece drawable : this.puzzleView.getPuzzlePieces()) {
                        this.lstOriginalDrawable.add(drawable.getDrawable());
                    }
                }
                new LoadFilterBitmap().execute(new Void[0]);
                slideDown(this.mRvTools);
                slideUp(this.filterLayout);
                slideUpSaveView();
                return;
            case STICKER:
                this.puzzleView.setTouchEnable(false);
                slideUpSaveView();
                slideDown(this.mRvTools);
                slideUp(this.stickerLayout);
                this.puzzleView.setLocked(false);
                this.puzzleView.setTouchEnable(false);
                return;
            case TEXT:
                this.puzzleView.setTouchEnable(false);
                slideUpSaveView();
                this.puzzleView.setLocked(false);
                openTextFragment();
                slideDown(this.mRvTools);
                slideUp(this.textLayout);
                this.addNewText.setVisibility(View.VISIBLE);
                return;
            case BACKGROUND:
                this.puzzleView.setLocked(false);
                this.puzzleView.setTouchEnable(false);
                slideUpSaveView();
                selectBackgroundColorTab();
                slideDown(this.mRvTools);
                slideUp(this.changeBackgroundLayout);
                showUpFunction(this.changeBackgroundLayout);
                if (this.puzzleView.getBackgroundResourceMode() == 0) {
                    this.currentBackgroundState.isColor = true;
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.drawableId = ((ColorDrawable) this.puzzleView.getBackground()).getColor();
                    return;
                } else if (this.puzzleView.getBackgroundResourceMode() == 2 || (this.puzzleView.getBackground() instanceof ColorDrawable)) {
                    this.currentBackgroundState.isBitmap = true;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.puzzleView.getBackground();
                    return;
                } else if (this.puzzleView.getBackground() instanceof GradientDrawable) {
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = (GradientDrawable) this.puzzleView.getBackground();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }


    public void loadPhoto() {
        final int i;
        final ArrayList arrayList = new ArrayList();
        if (this.lstPaths.size() > this.puzzleLayout.getAreaCount()) {
            i = this.puzzleLayout.getAreaCount();
        } else {
            i = this.lstPaths.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r3 = new Target() {
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                public void onPrepareLoad(Drawable drawable) {
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    boolean z = width > height;
                    if (1920 < Math.min(height, width)) {
                        float f = !z ? 1920.0f / ((float) width) : 1920.0f / ((float) height);
                        bitmap = FileUtils.getResizedBitmap(bitmap, (int) (((float) width) * f), (int) (((float) height) * f));
                    }
                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (CollageMakerActivity.this.lstPaths.size() < CollageMakerActivity.this.puzzleLayout.getAreaCount()) {
                            for (int i = 0; i < CollageMakerActivity.this.puzzleLayout.getAreaCount(); i++) {
                                CollageMakerActivity.this.puzzleView.addPiece((Bitmap) arrayList.get(i % i));
                            }
                        } else {
                            CollageMakerActivity.this.puzzleView.addPieces(arrayList);
                        }
                    }
                    CollageMakerActivity.this.targets.remove(this);
                    CollageMakerActivity.this.stickerLayout.setVisibility(View.VISIBLE);
                }
            };
            Picasso picasso = Picasso.get();
            picasso.load("file:///" + this.lstPaths.get(i2)).resize(this.deviceWidth, this.deviceWidth).centerInside().config(Bitmap.Config.RGB_565).into((Target) r3);
            this.targets.add(r3);
        }
    }

    public void slideUp(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{(float) view.getHeight(), 0.0f}).start();
    }


    public void onDestroy() {
        super.onDestroy();
        try {
            this.puzzleView.reset();
        } catch (Exception unused) {
        }
    }

    public void slideUpSaveView() {
        this.saveControl.setVisibility(View.GONE);
    }

    public void slideDownSaveView() {
        this.saveControl.setVisibility(View.VISIBLE);
    }

    public void slideDown(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{0.0f, (float) view.getHeight()}).start();
    }

    public void onBackPressed() {
        if (this.currentMode == null) {
            CollageMakerActivity.this.finish();

            return;
        }
        try {
            switch (this.currentMode) {
                case LAYOUT:
                case BORDER:
                case RATIO:
                    slideDown(this.changeLayoutLayout);
                    slideUp(this.mRvTools);
                    slideDownSaveView();
                    showDownFunction();
                    this.puzzleView.updateLayout(this.puzzleLayout);
                    this.puzzleView.setPiecePadding(this.piecePadding);
                    this.puzzleView.setPieceRadian(this.pieceBorderRadius);
                    this.currentMode = ToolType.NONE;
                    getWindowManager().getDefaultDisplay().getSize(new Point());
                    onNewAspectRatioSelected(this.currentAspect);
                    this.puzzleView.setAspectRatio(this.currentAspect);
                    this.puzzleView.setLocked(true);
                    this.puzzleView.setTouchEnable(true);
                    return;
                case FILTER:
                    slideUp(this.mRvTools);
                    slideDown(this.filterLayout);
                    for (int i = 0; i < this.lstOriginalDrawable.size(); i++) {
                        this.puzzleView.getPuzzlePieces().get(i).setDrawable(this.lstOriginalDrawable.get(i));
                    }
                    this.puzzleView.invalidate();
                    slideDownSaveView();
                    this.currentMode = ToolType.NONE;
                    return;
                case STICKER:
                    if (this.puzzleView.getStickers().size() <= 0) {
                        slideUp(this.wrapStickerList);
                        slideDown(this.stickerLayout);
                        this.addNewSticker.setVisibility(View.GONE);
                        this.puzzleView.setHandlingSticker((Sticker) null);
                        slideUp(this.mRvTools);
                        this.puzzleView.setLocked(true);
                        this.currentMode = ToolType.NONE;
                    } else if (this.addNewSticker.getVisibility() == 0) {
                        this.puzzleView.getStickers().clear();
                        this.addNewSticker.setVisibility(View.GONE);
                        this.puzzleView.setHandlingSticker((Sticker) null);
                        slideUp(this.wrapStickerList);
                        slideDown(this.stickerLayout);
                        slideUp(this.mRvTools);
                        this.puzzleView.setLocked(true);
                        this.puzzleView.setTouchEnable(true);
                        this.currentMode = ToolType.NONE;
                    } else {
                        slideDown(this.wrapStickerList);
                        this.addNewSticker.setVisibility(View.VISIBLE);
                    }
                    slideDownSaveView();
                    return;
                case TEXT:
                    if (!this.puzzleView.getStickers().isEmpty()) {
                        this.puzzleView.getStickers().clear();
                        this.puzzleView.setHandlingSticker((Sticker) null);
                    }
                    slideDown(this.textLayout);
                    this.addNewText.setVisibility(View.GONE);
                    this.puzzleView.setHandlingSticker((Sticker) null);
                    slideUp(this.mRvTools);
                    slideDownSaveView();
                    this.puzzleView.setLocked(true);
                    this.currentMode = ToolType.NONE;
                    this.puzzleView.setTouchEnable(true);
                    return;
                case BACKGROUND:
                    slideUp(this.mRvTools);
                    slideDown(this.changeBackgroundLayout);
                    this.puzzleView.setLocked(true);
                    this.puzzleView.setTouchEnable(true);
                    if (this.currentBackgroundState.isColor) {
                        this.puzzleView.setBackgroundResourceMode(0);
                        this.puzzleView.setBackgroundColor(this.currentBackgroundState.drawableId);
                    } else if (this.currentBackgroundState.isBitmap) {
                        this.puzzleView.setBackgroundResourceMode(2);
                        this.puzzleView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.puzzleView.setBackgroundResourceMode(1);
                        if (this.currentBackgroundState.drawable != null) {
                            this.puzzleView.setBackground(this.currentBackgroundState.drawable);
                        } else {
                            this.puzzleView.setBackgroundResource(this.currentBackgroundState.drawableId);
                        }
                    }
                    slideDownSaveView();
                    showDownFunction();
                    this.currentMode = ToolType.NONE;
                    return;
                case PIECE:
                    slideDown(this.rvPieceControl);
                    slideUp(this.mRvTools);
                    slideDownSaveView();
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.rvPieceControl.getLayoutParams();
                    layoutParams.bottomMargin = 0;
                    this.rvPieceControl.setLayoutParams(layoutParams);
                    this.currentMode = ToolType.NONE;
                    this.puzzleView.setHandlingPiece((PuzzlePiece) null);
                    this.puzzleView.setPreviousHandlingPiece((PuzzlePiece) null);
                    this.puzzleView.invalidate();
                    this.currentMode = ToolType.NONE;
                    return;
                case NONE:
                    Toast.makeText(getApplicationContext(), getString(R.string.press_one_more_time), 0).show();
                    this.currentMode = null;
                    return;
                default:
                    super.onBackPressed();

                    return;
            }
        } catch (Exception unused) {
        }
    }

    public void onItemClick(PuzzleLayout puzzleLayout2, int i) {
        PuzzleLayout parse = PuzzleLayoutParser.parse(puzzleLayout2.generateInfo());
        puzzleLayout2.setRadian(this.puzzleView.getPieceRadian());
        puzzleLayout2.setPadding(this.puzzleView.getPiecePadding());
        this.puzzleView.updateLayout(parse);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        this.puzzleView.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.wrapPuzzleView);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.puzzleView.getId(), 3, this.wrapPuzzleView.getId(), 3, 0);
        constraintSet2.connect(this.puzzleView.getId(), 1, this.wrapPuzzleView.getId(), 1, 0);
        constraintSet2.connect(this.puzzleView.getId(), 4, this.wrapPuzzleView.getId(), 4, 0);
        constraintSet2.connect(this.puzzleView.getId(), 2, this.wrapPuzzleView.getId(), 2, 0);
        constraintSet.applyTo(this.wrapPuzzleView);
        this.puzzleView.setAspectRatio(aspectRatio);
    }

    public void replaceCurrentPiece(String str) {
        new OnLoadBitmapFromUri().execute(new String[]{str});
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = this.wrapPuzzleView.getHeight();
        if (aspectRatio.getHeight() > aspectRatio.getWidth()) {
            int ratio = (int) (aspectRatio.getRatio() * ((float) height));
            if (ratio < point.x) {
                return new int[]{ratio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getRatio())};
        }
        int ratio2 = (int) (((float) point.x) / aspectRatio.getRatio());
        if (ratio2 > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getRatio()), height};
        }
        return new int[]{point.x, ratio2};
    }

    public void onPause() {
        super.onPause();
        this.keyboardHeightProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardHeightProvider.onResume();
    }

    public void addSticker(Bitmap bitmap) {
        this.puzzleView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        slideDown(this.wrapStickerList);
        this.addNewSticker.setVisibility(View.VISIBLE);
    }

    public void onBackgroundSelected(final PuzzleBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.puzzleView.setBackgroundColor(squareView.drawableId);
            this.puzzleView.setBackgroundResourceMode(0);
        } else if (squareView.drawable != null) {
            this.puzzleView.setBackgroundResourceMode(2);
            new AsyncTask<Void, Bitmap, Bitmap>() {

                public void onPreExecute() {
                    CollageMakerActivity.this.showLoading(true);
                }


                public Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }


                public void onPostExecute(Bitmap bitmap) {
                    CollageMakerActivity.this.showLoading(false);
                    CollageMakerActivity.this.puzzleView.setBackground(new BitmapDrawable(CollageMakerActivity.this.getResources(), bitmap));
                }
            }.execute(new Void[0]);
        } else {
            this.puzzleView.setBackgroundResource(squareView.drawableId);
            this.puzzleView.setBackgroundResourceMode(1);
        }
    }

    public void onFilterSelected(String str) {
        new LoadBitmapWithFilter().execute(new String[]{str});
    }

    public void onPieceFuncSelected(ToolType toolType) {
        switch (toolType) {
            case REPLACE:
                PhotoPicker.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardMain(true).start(this);
                return;
            case H_FLIP:
                this.puzzleView.flipHorizontally();
                return;
            case V_FLIP:
                this.puzzleView.flipVertically();
                return;
            case ROTATE:
                this.puzzleView.rotate(90.0f);
                return;
            case CROP:
                CropDialogFragment.show(this, this, ((BitmapDrawable) this.puzzleView.getHandlingPiece().getDrawable()).getBitmap());
                return;
            case FILTER:
                new LoadFilterBitmapForCurrentPiece().execute();

        }
    }

    public void finishCrop(Bitmap bitmap) {
        this.puzzleView.replace(bitmap, "");
    }

    public void onSaveFilter(Bitmap bitmap) {
        this.puzzleView.replace(bitmap, "");
    }

    class LoadFilterBitmap extends AsyncTask<Void, Void, Void> {
        LoadFilterBitmap() {
        }


        public void onPreExecute() {
            CollageMakerActivity.this.showLoading(true);
        }


        public Void doInBackground(Void... voidArr) {
            CollageMakerActivity.this.lstBitmapWithFilter.clear();
            CollageMakerActivity.this.lstBitmapWithFilter.addAll(FilterUtils.getLstBitmapWithFilter(ThumbnailUtils.extractThumbnail(((BitmapDrawable) CollageMakerActivity.this.puzzleView.getPuzzlePieces().get(0).getDrawable()).getBitmap(), 150, 150)));
            return null;
        }


        public void onPostExecute(Void voidR) {
            CollageMakerActivity.this.rvFilterView.setAdapter(new FilterViewAdapter(CollageMakerActivity.this.lstBitmapWithFilter, CollageMakerActivity.this, CollageMakerActivity.this.getApplicationContext(), Arrays.asList(FilterUtils.EFFECT_CONFIGS)));
            CollageMakerActivity.this.slideDown(CollageMakerActivity.this.mRvTools);
            CollageMakerActivity.this.slideUp(CollageMakerActivity.this.filterLayout);
            CollageMakerActivity.this.showLoading(false);
        }
    }

    class LoadFilterBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadFilterBitmapForCurrentPiece() {
        }


        public void onPreExecute() {
            CollageMakerActivity.this.showLoading(true);
        }


        public List<Bitmap> doInBackground(Void... voidArr) {
            return FilterUtils.getLstBitmapWithFilter(ThumbnailUtils.extractThumbnail(((BitmapDrawable) CollageMakerActivity.this.puzzleView.getHandlingPiece().getDrawable()).getBitmap(), 150, 150));
        }


        public void onPostExecute(List<Bitmap> list) {
            CollageMakerActivity.this.showLoading(false);
            try {
                FilterDialogFragment.show(CollageMakerActivity.this, CollageMakerActivity.this, ((BitmapDrawable) CollageMakerActivity.this.puzzleView.getHandlingPiece().getDrawable()).getBitmap(), list);
            } catch (Exception unused) {
            }
        }
    }

    class LoadBitmapWithFilter extends AsyncTask<String, List<Bitmap>, List<Bitmap>> {
        LoadBitmapWithFilter() {
        }


        public void onPreExecute() {
            CollageMakerActivity.this.showLoading(true);
        }


        public List<Bitmap> doInBackground(String... strArr) {
            ArrayList arrayList = new ArrayList();
            for (Drawable drawable : CollageMakerActivity.this.lstOriginalDrawable) {
                arrayList.add(FilterUtils.getBitmapWithFilter(((BitmapDrawable) drawable).getBitmap(), strArr[0]));
            }
            return arrayList;
        }


        public void onPostExecute(List<Bitmap> list) {
            for (int i = 0; i < list.size(); i++) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(CollageMakerActivity.this.getResources(), list.get(i));
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);
                CollageMakerActivity.this.puzzleView.getPuzzlePieces().get(i).setDrawable(bitmapDrawable);
            }
            CollageMakerActivity.this.puzzleView.invalidate();
            CollageMakerActivity.this.showLoading(false);
        }
    }

    class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }


        public void onPreExecute() {
            CollageMakerActivity.this.showLoading(true);
        }


        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));
                return SystemUtil.rotateBitmap(MediaStore.Images.Media.getBitmap(CollageMakerActivity.this.getContentResolver(), fromFile), new ExifInterface(CollageMakerActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            CollageMakerActivity.this.showLoading(false);
            CollageMakerActivity.this.puzzleView.replace(bitmap, "");
        }
    }

    class SavePuzzleAsFile extends AsyncTask<Bitmap, String, String> {
        SavePuzzleAsFile() {
        }


        public void onPreExecute() {
            CollageMakerActivity.this.showLoading(true);
        }


        public String doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            Bitmap bitmap2 = bitmapArr[1];
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawBitmap(bitmap, (Rect) null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), (Paint) null);
            canvas.drawBitmap(bitmap2, (Rect) null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), (Paint) null);
            bitmap.recycle();
            bitmap2.recycle();


            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri imageUri = CollageMakerActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (imageUri != null) {
                try {
                    OutputStream out = CollageMakerActivity.this.getContentResolver().openOutputStream(imageUri);
                    createBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }

        public void onPostExecute(String str) {
            CollageMakerActivity.this.showLoading(false);
            Intent intent = new Intent(CollageMakerActivity.this, FinalActivity.class);
            CollageMakerActivity.this.startActivity(intent);
            finish();


        }
    }

}
