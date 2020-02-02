package com.yunbiao.armyface_offline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.db.User;

import java.util.List;


/**
 * Created by Administrator on 2018/9/17.
 */

public class PersonAdapter extends BaseAdapter {
    private static final String TAG = "EmployAdapter";
    private Context context;
    private List<User> mlist;

    public PersonAdapter(Context context, List<User> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public User getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_employ, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindData(mlist.get(position));

        return convertView;
    }

    class ViewHolder {
        TextView tv_no;
        TextView tv_name;
        TextView tv_company_name;
        TextView tv_depart_name;
        TextView tv_status;
        ImageView iv_head;

        public ViewHolder(View convertView) {
            tv_no = convertView.findViewById(R.id.tv_no);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_company_name = convertView.findViewById(R.id.tv_company_name);
            tv_depart_name = convertView.findViewById(R.id.tv_depart_name);
            tv_status = convertView.findViewById(R.id.tv_status);
            iv_head = convertView.findViewById(R.id.iv_head);
        }

        public void bindData(User user) {
            tv_no.setText(user.getUserCode());
            tv_name.setText(user.getUserName());
            tv_company_name.setText(user.getCompanyName());
            tv_depart_name.setText(user.getDeptName());
            tv_status.setText(TextUtils.equals("1", user.getUserStatus()) ? "正常" : "禁用");
            Glide.with(context).load(user.getIcon()).asBitmap().override(80,80).into(iv_head);
        }
    }
}
