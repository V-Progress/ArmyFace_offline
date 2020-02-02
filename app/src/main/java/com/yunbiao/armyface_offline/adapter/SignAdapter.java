package com.yunbiao.armyface_offline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.db.Sign;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17.
 */

public class SignAdapter extends BaseAdapter {
    private static final String TAG = "SignAdapter";
    private Context context;
    private List<Sign> mlist;

    public SignAdapter(Context context, List<Sign> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Sign getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_sign, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindData(mlist.get(position));
        return convertView;
    }

    class ViewHolder {

        private final TextView tvNo;
        private final TextView tvName;
        private final TextView tvCompanyName;
        private final TextView tvSignTime;
        private final TextView tvIcNo;
        private final ImageView ivSignHead;

        public ViewHolder(View convertView) {
            tvNo = convertView.findViewById(R.id.tv_no);
            tvName = convertView.findViewById(R.id.tv_name);
            tvCompanyName = convertView.findViewById(R.id.tv_company_name);
            tvSignTime = convertView.findViewById(R.id.tv_sign_time);
            tvIcNo = convertView.findViewById(R.id.tv_ic_No);
            ivSignHead = convertView.findViewById(R.id.iv_sign_head);
        }

        public void bindData(Sign sign) {
            tvNo.setText(sign.getUserCode());
            tvName.setText(sign.getUserName());
            tvCompanyName.setText(sign.getCompanyName());
            tvIcNo.setText(sign.getIcNo());

            String startTime = sign.getStartTime();
            String endTime = sign.getEndTime();
            if (!TextUtils.isEmpty(endTime)) {
                tvSignTime.setText(endTime);
            } else {
                tvSignTime.setText(startTime);
            }

            Glide.with(context).load(sign.getSignIcon()).asBitmap().override(60, 60).into(ivSignHead);
        }
    }
}
