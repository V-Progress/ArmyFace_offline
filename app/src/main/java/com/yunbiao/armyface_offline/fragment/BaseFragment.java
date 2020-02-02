package com.yunbiao.armyface_offline.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    protected int mOrientation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mOrientation = getActivity().getResources().getConfiguration().orientation;

        View view;
        if(mOrientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(getLayout_s(), container, false);
        } else {
            view = inflater.inflate(getLayout_h(), container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initView();

        initData();
    }

    protected abstract int getLayout_h();

    protected abstract int getLayout_s();

    protected abstract void initView();

    protected abstract void initData();

    protected <T extends View>T fv(int id){
        return getView().findViewById(id);
    }
}
