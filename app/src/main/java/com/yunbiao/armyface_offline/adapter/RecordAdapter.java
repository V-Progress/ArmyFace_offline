package com.yunbiao.armyface_offline.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunbiao.armyface_offline.R;
import com.yunbiao.armyface_offline.db.Sign;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Sign> signBeanList;
    private Context mContext;
    private int orientation;
    private int id;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public RecordAdapter(Context context, List<Sign> signBeanList, int currOrientation) {
        this.mContext = context;
        this.signBeanList = signBeanList;
        this.orientation = currOrientation;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return new ViewHolder(View.inflate(viewGroup.getContext(), R.layout.item_sign_main, null));
        } else {
            return new ViewHolder(View.inflate(viewGroup.getContext(), R.layout.item_sign_h, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;

        vh.bindData(signBeanList.get(i));
    }

    @Override
    public int getItemCount() {
        return signBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHead;
        TextView tvName;
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head_item);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvTime = itemView.findViewById(R.id.tv_time_item);
        }

        public void bindData(Sign sign) {

            tvName.setText(sign.getUserName());

            Glide.with(mContext).load(sign.getIcon()).asBitmap().override(60, 60).transform(new RoundedCornersTransformation(mContext,8,3)).into(ivHead);

            String startTime = sign.getStartTime();
            String endTime = sign.getEndTime();
            String time;
            if (!TextUtils.isEmpty(endTime)) {
                time = endTime.substring(11);
            } else {
                time = startTime.substring(11);
            }
            tvTime.setText(time);
        }
    }

}
