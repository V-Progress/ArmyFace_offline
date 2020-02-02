package com.yunbiao.armyface_offline.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.yunbiao.armyface_offline.Path;
import com.yunbiao.armyface_offline.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class InsideFragment extends BaseFragment {
    private static final String TAG = "InsideFragment";
    private ImageView ivInfo;
    private VerticalViewPager viewPager;
    private int mPlayTime = 10;
    private ContentFragmentAdapter mAdapter;

    @Override
    protected int getLayout_h() {
        return R.layout.fragment_inside;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.fragment_inside;
    }

    @Override
    protected void initView() {
        ivInfo = fv(R.id.iv_info);
        viewPager = fv(R.id.vp_img);
        viewPager.setPageTransformer(false, new DefaultTransformer());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "onTouch: 触摸重新计时");
                viewPager.removeCallbacks(playRunnable);
                viewPager.postDelayed(playRunnable, mPlayTime * 1000);
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        String infoPath = Path.INFO_PATH;

        File infoFile = new File(infoPath);
        File[] files = infoFile.listFiles();
        if (files == null || files.length <= 0) {
            viewPager.setVisibility(View.GONE);
            ivInfo.setVisibility(View.VISIBLE);
            return;
        } else {
            viewPager.setVisibility(View.VISIBLE);
            ivInfo.setVisibility(View.GONE);

            ContentFragmentAdapter.Holder holder = new ContentFragmentAdapter.Holder(getChildFragmentManager());
            for (File file : files) {
                holder.add(ContentFragment.newInstance(file.getPath()));
            }
            mAdapter = holder.set();
            viewPager.setAdapter(mAdapter);
            viewPager.postDelayed(playRunnable, mPlayTime * 1000);
        }
    }

    private Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAdapter == null || mAdapter.getCount() <= 1) {
                return;
            }

            int currentItem = viewPager.getCurrentItem();
            currentItem += 1;
            if (currentItem >= mAdapter.getCount()) {
                currentItem = 0;
            }
            viewPager.setCurrentItem(currentItem, true);

            viewPager.postDelayed(playRunnable, mPlayTime * 1000);
        }
    };

    public static class ContentFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public ContentFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getId() + "";
        }

        public static class Holder {
            private final List<Fragment> fragments = new ArrayList<>();
            private FragmentManager manager;

            public Holder(FragmentManager manager) {
                this.manager = manager;
            }

            public Holder add(Fragment f) {
                fragments.add(f);
                return this;
            }

            public ContentFragmentAdapter set() {
                return new ContentFragmentAdapter(manager, fragments);
            }
        }
    }
}
