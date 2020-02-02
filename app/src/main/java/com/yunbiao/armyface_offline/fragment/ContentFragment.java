package com.yunbiao.armyface_offline.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yunbiao.armyface_offline.R;

public class ContentFragment extends Fragment {

    public static String KEY_URL = "keyUrl";
    private ImageView ivContent;

    public static ContentFragment newInstance(String url) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_inside_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivContent = getView().findViewById(R.id.iv_content);
        String url = getArguments().getString(KEY_URL);

        Glide.with(getActivity())
                .load(url)
                .crossFade()
//                .override(ivContent.getWidth(), ivContent.getHeight())
                .into(ivContent);
    }
}
