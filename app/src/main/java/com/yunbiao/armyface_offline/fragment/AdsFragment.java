package com.yunbiao.armyface_offline.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.R;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class AdsFragment extends BaseFragment {
    private static final String TAG = "AdsFragment";

    private View adsView;
    private int mOpenTime = 10;

    private int mAdsTime = 10;

    private PropertyValuesHolder animY;
    private ObjectAnimator objectAnimator;

    private Queue<File> fileQueue = new LinkedList<>();
    private ImageView ivAds;

    @Override
    protected int getLayout_h() {
        return R.layout.fragment_ads;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.fragment_ads;
    }

    @Override
    protected void initView() {
        ivAds = fv(R.id.iv_ads);
        adsView = fv(R.id.fl_ads);
        adsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
            }
        });

        adsView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            adsView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            closeView();
        }
    };

    @Override
    protected void initData() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            ivAds.setImageResource(R.mipmap.img_wel);
        } else {
            ivAds.setImageResource(R.mipmap.img_wel_h);
        }

        String adsPath = Path.ADS_PATH;

        File adsFile = new File(adsPath);
        File[] files = adsFile.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }

        fileQueue.clear();
        fileQueue.addAll(Arrays.asList(files));
    }

    public void detectFace() {
        closeView();
    }

    private void closeView() {
        if (adsView.isShown()) {
            if (objectAnimator != null && objectAnimator.isRunning()) {
                return;
            }
            startAnim(adsView, 0, adsView.getMeasuredHeight(), new Runnable() {
                @Override
                public void run() {
                    adsView.setVisibility(View.GONE);
                    //发送计时任务
                    handler.removeMessages(0);
                    handler.sendEmptyMessage(0);

                    //广告关闭后删除回调
                    adsView.removeCallbacks(adsRunnable);
                }
            });
        }
        mOpenTime = 10;
    }

    private void openView() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            return;
        }
        adsView.setVisibility(View.VISIBLE);
        startAnim(adsView, adsView.getMeasuredHeight(), 0, new Runnable() {
            @Override
            public void run() {
                //广告开启后删除计时任务
                handler.removeMessages(0);
                //开始广告回调
                adsView.removeCallbacks(adsRunnable);
                adsView.post(adsRunnable);
            }
        });
    }

    private void startAnim(final View view, int formY, int toY, final Runnable runnable) {
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开始动画前开启硬件加速
        animY = PropertyValuesHolder.ofFloat("translationY", formY, toY);//生成值动画
        //加载动画Holder
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, animY);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setLayerType(View.LAYER_TYPE_NONE, null);//动画结束时关闭硬件加速
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        objectAnimator.start();
    }

    private Runnable adsRunnable = new Runnable() {
        @Override
        public void run() {
            if (fileQueue.size() <= 0) {
                return;
            }
            File file = fileQueue.poll();
            Glide.with(getActivity()).load(file).crossFade().into(ivAds);
            fileQueue.offer(file);
            if (fileQueue.size() == 1) {
                return;
            }
            adsView.postDelayed(adsRunnable, mAdsTime * 1000);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mOpenTime <= 0) {
                mOpenTime = 10;
                if (!adsView.isShown()) {
                    openView();
                }
                return;
            } else {
                mOpenTime--;
            }

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
}