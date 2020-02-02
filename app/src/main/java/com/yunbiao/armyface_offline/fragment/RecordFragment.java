package com.yunbiao.armyface_offline.fragment;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.adapter.RecordAdapter;
import com.yunbiao.armyface_offline.db.DaoManager;
import com.yunbiao.armyface_offline.db.Sign;
import com.yunbiao.armyface_offline.db.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RecordFragment extends BaseFragment {

    private RecyclerView rlvRecord;
    private RecordAdapter recordAdapter;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private List<Sign> showList = new ArrayList<>();
    private List<String> alreadyList = new ArrayList<>();
    private TextView tvAlready;
    private TextView tvTotal;

    @Override
    protected int getLayout_h() {
        return R.layout.fragment_record_h;
    }

    @Override
    protected int getLayout_s() {
        return R.layout.fragment_record;
    }

    @Override
    protected void initView() {
        rlvRecord = fv(R.id.rlv_record);
        tvTotal = fv(R.id.tv_total_number);
        tvAlready = fv(R.id.tv_already_number);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), mOrientation == Configuration.ORIENTATION_PORTRAIT ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false);
        rlvRecord.setLayoutManager(linearLayoutManager);
        rlvRecord.setItemAnimator(new DefaultItemAnimator());
        rlvRecord.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = 10;
                outRect.right = 10;
                outRect.bottom = 10;
            }
        });

        recordAdapter = new RecordAdapter(getActivity(), showList, mOrientation);
        rlvRecord.setAdapter(recordAdapter);
    }

    @Override
    protected void initData() {
        showList.clear();
        alreadyList.clear();

        Date date = new Date();
        String currDate = dateFormat.format(date);

        List<Sign> signs = DaoManager.get().querySignByDate(currDate);
        if (signs == null) {
            signs = new ArrayList<>();
        }
        Collections.reverse(signs);

        showList.addAll(signs);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<User> userList = DaoManager.get().queryAll(User.class);
        if (userList != null) {
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                User next = iterator.next();
                if (TextUtils.equals("1", next.getUserStatus())) {
                    continue;
                }
                iterator.remove();
            }
            tvTotal.setText(userList.size() + "");
        } else {
            tvTotal.setText("0");
        }

        for (Sign sign : showList) {
            String userCode = sign.getUserCode();
            if (alreadyList.contains(userCode)) {
                continue;
            }
            alreadyList.add(userCode);
        }
        tvAlready.setText(alreadyList.size() + "");
    }

    public void updateList(final Sign sign) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showList.add(0, sign);
                recordAdapter.notifyItemInserted(0);
                rlvRecord.scrollToPosition(0);

                updateNumber(sign.getUserCode());
            }
        });
    }

    private void updateNumber(String userCode) {
        if (alreadyList.contains(userCode)) {
            return;
        }
        alreadyList.add(userCode);
        tvAlready.setText(alreadyList.size() + "");
    }
}
