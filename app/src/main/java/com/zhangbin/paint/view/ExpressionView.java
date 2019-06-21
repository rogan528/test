package com.zhangbin.paint.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sanhai.live.entity.ExpressionEntity;
import com.sanhai.live.event.OnExpressionListener;
import com.sanhai.live.module.OrderBean;
import com.sanhai.live.util.DimensionUtils;
import com.sanhai.live.util.ExpressionUtil;
import com.sanhai.live.util.Util;
import com.zhangbin.paint.R;
import com.zhangbin.paint.adapter.ExpressionAdapter;
import com.zhangbin.paint.adapter.ViewPagerAdapter;
import com.sanhai.live.module.ExpressionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class ExpressionView extends FrameLayout implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ViewPager mPagerContainer;
    /**
     * 表情适配器列表
     */
    private List<ExpressionAdapter> mEmoAdapters;
    private List<View> mPageViews;
    /**
     * 每页列数
     */
    private int mPageColumn = 7;
    private int padding;
    private ExpressionData mDataAction;
    /**
     * 当前页
     */
    private int mCurrentPage;
    /*private String[] expressionChars = {"[哈哈]", "[嘻嘻]", "[good]", "[love]", "[flower]", "[cool]", "[why]", "[pitiful]", "[amaz]", "[bye]"};
    private int[] expressionResIds = {R.mipmap.aha, R.mipmap.hard, R.mipmap.good, R.mipmap.love, R.mipmap.flower,
            R.mipmap.cool,R.mipmap.why, R.mipmap.pitiful, R.mipmap.amaz, R.mipmap.bye};*/
    public static Map<String,Integer> map = new HashMap();

    private String[] expressionChars = {"[呵呵]",
            "[嘻嘻]", "[哈哈]", "[爱你]", "[晕]", "[泪]", "[馋嘴]", "[抓狂]", "[哼]", "[可爱]", "[怒]",
            "[汗]", "[困]", "[害羞]", "[睡觉]", "[钱]", "[偷笑]", "[酷]", "[衰]", "[吃惊]", "[闭嘴]",
            "[鄙视]", "[挖鼻屎]", "[花心]", "[鼓掌]", "[失望]", "[思考]", "[生病]", "[亲亲]", "[怒骂]", "[太开心]",
            "[懒得理你]", "[右哼哼]", "[左哼哼]", "[嘘]", "[委屈]", "[吐]", "[可怜]", "[打哈气]", "[做鬼脸]", "[握手]",
            "[耶]", "[good]", "[弱]", "[不要]", "[ok]", "[赞]", "[来]", "[蛋糕]", "[心]", "[伤心]",
            "[猪头]", "[咖啡]", "[话筒]", "[干杯]", "[绿丝带]", "[蜡烛]", "[微风]", "[月亮]", "[挤眼]", "[书呆子]",
            "[黑线]", "[疑问]", "[阴险]", "[顶]", "[悲伤]", "[抱抱]", "[拜拜]", "[愤怒]", "[感冒]", "[haha]",
            "[拳头]", "[最差]", "[右抱抱]", "[左抱抱]","[花]"

    };
    private int[] expressionResIds = {R.mipmap.emotion_000,
            R.mipmap.emotion_001, R.mipmap.emotion_002, R.mipmap.emotion_003, R.mipmap.emotion_004, R.mipmap.emotion_005,R.mipmap.emotion_006, R.mipmap.emotion_007, R.mipmap.emotion_008, R.mipmap.emotion_009, R.mipmap.emotion_010,
            R.mipmap.emotion_011, R.mipmap.emotion_012, R.mipmap.emotion_013, R.mipmap.emotion_014, R.mipmap.emotion_015, R.mipmap.emotion_016,R.mipmap.emotion_017, R.mipmap.emotion_018, R.mipmap.emotion_019, R.mipmap.emotion_020,
            R.mipmap.emotion_021, R.mipmap.emotion_022, R.mipmap.emotion_023, R.mipmap.emotion_024, R.mipmap.emotion_025, R.mipmap.emotion_026, R.mipmap.emotion_027,R.mipmap.emotion_028, R.mipmap.emotion_029, R.mipmap.emotion_030,
            R.mipmap.emotion_031, R.mipmap.emotion_032, R.mipmap.emotion_033, R.mipmap.emotion_034, R.mipmap.emotion_035, R.mipmap.emotion_036, R.mipmap.emotion_037, R.mipmap.emotion_038,R.mipmap.emotion_039, R.mipmap.emotion_040,
            R.mipmap.emotion_041, R.mipmap.emotion_042, R.mipmap.emotion_043,R.mipmap.emotion_044, R.mipmap.emotion_045, R.mipmap.emotion_046, R.mipmap.emotion_047, R.mipmap.emotion_048,R.mipmap.emotion_049, R.mipmap.emotion_050,
            R.mipmap.emotion_052, R.mipmap.emotion_053, R.mipmap.emotion_054, R.mipmap.emotion_055,R.mipmap.emotion_056, R.mipmap.emotion_057, R.mipmap.emotion_058, R.mipmap.emotion_059, R.mipmap.emotion_060,R.mipmap.emotion_067,
            R.mipmap.emotion_071, R.mipmap.emotion_081, R.mipmap.emotion_082, R.mipmap.emotion_086,R.mipmap.emotion_094, R.mipmap.emotion_096, R.mipmap.emotion_097, R.mipmap.emotion_107, R.mipmap.emotion_108,R.mipmap.emotion_113,
            R.mipmap.emotion_116, R.mipmap.emotion_121, R.mipmap.emotion_122, R.mipmap.emotion_123,R.mipmap.emotion_168
    };

    public ExpressionView(Context context, int pageColumn) {
        super(context);
        mPageColumn = pageColumn;
        init(null, 0);
    }

    public ExpressionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ExpressionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        try {
            mContext = this.getContext();
            padding = DimensionUtils.dip2px(mContext, 5);
            mDataAction = new ExpressionData();
            mDataAction.parseData(expressionChars, expressionResIds);
            initUI();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化map数据
     */
    private void initData() {
        int length = expressionChars.length;
        for(int i=0;i<length;i++){
            String key = ExpressionUtil.getIMEmotionString(mContext,expressionChars[i],"mipmap");
            map.put(key,expressionResIds[i]);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initViews();
        initViewPager();
    }

    /**
     * 初始化iewPager
     */
    private void initViewPager() {
        mPagerContainer = new ViewPager(mContext);
        this.addView(mPagerContainer);
        ViewPagerAdapter pageAdapeter = new ViewPagerAdapter(mPageViews);
        mPagerContainer.setAdapter(pageAdapeter);
        mPagerContainer.setCurrentItem(0);
        mPagerContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrentPage = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /***
     * 初始化表情GridView
     */
    private void initViews() {
        if (mEmoAdapters == null)
            mEmoAdapters = new ArrayList<ExpressionAdapter>();
        if (mPageViews == null)
            mPageViews = new ArrayList<View>();
        GridView gridView;
        ExpressionAdapter emoAdapter;
        for (int i = 0, len = mDataAction.getPageEmoEntitys().size(); i < len; i++) {
            gridView = new GridView(mContext);
            emoAdapter = new ExpressionAdapter(mContext, mPageColumn);
            emoAdapter.setItems(mDataAction.getPageEmoEntitys().get(i));
            gridView.setAdapter(emoAdapter);
            mEmoAdapters.add(emoAdapter);

            gridView.setNumColumns(mPageColumn);
            gridView.setBackgroundColor(Color.TRANSPARENT);
            gridView.setHorizontalSpacing(1);
            gridView.setVerticalSpacing(1);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView.setCacheColorHint(0);
            gridView.setPadding(padding, padding, padding, padding);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gridView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            gridView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
            gridView.setOnItemClickListener(this);
            gridView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mPageViews.add(gridView);
        }
    }

    private OnExpressionListener mListener;

    /**
     * 设置选中表情监听事件回调接口
     */
    public void setOnEmotionSelectedListener(OnExpressionListener listener) {
        mListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpressionEntity entry = (ExpressionEntity) mEmoAdapters.get(mCurrentPage).getItem(position);
        if (entry != null && !TextUtils.isEmpty(entry.character)) {

            if (mListener != null) {
                if (entry.resId == R.drawable.iv_delete_expression) {
                    mListener.OnExpressionRemove();
                } else {
                    mListener.OnExpressionSelected(entry);
                }
            }
        }
    }
}
