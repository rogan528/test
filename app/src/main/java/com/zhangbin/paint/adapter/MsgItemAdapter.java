package com.zhangbin.paint.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijian.sf_im_sdk.MsgContent;
import com.sanhai.live.util.ExpressionUtil;
import com.zhangbin.paint.R;
import com.sanhai.live.util.TimeUtil;
import com.zhangbin.paint.view.ExpressionView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MsgItemAdapter extends  ArrayAdapter<MsgContent> {

    private int layoutId;
    private Context context;
    public MsgItemAdapter(Context context, int layoutId, List<MsgContent> list) {
        super(context, layoutId, list);
        this.context = context;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgContent item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);

        TextView textView1 = (TextView) view.findViewById(R.id.item_text1);
        TextView showTime = (TextView) view.findViewById(R.id.item_time);
        TextView showMsg = (TextView) view.findViewById(R.id.item_msg);

        textView1.setText(item.getName()+":");

       long time = Long.parseLong(item.getMsgtime());
       String timeStrampStr = TimeUtil.displayDuration(time);
        showTime.setText(timeStrampStr);
        SpannableString expressionString = ExpressionUtil.getExpressionString(context, ExpressionView.map,item.getMsgData(), "mipmap");
        showMsg.setText(expressionString);
       return view;
    }
}
