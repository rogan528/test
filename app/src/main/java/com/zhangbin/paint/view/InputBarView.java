package com.zhangbin.paint.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sanhai.live.entity.ExpressionEntity;
import com.sanhai.live.event.OnExpressionListener;
import com.sanhai.live.event.OnSendFlowerListener;
import com.sanhai.live.util.DimensionUtils;
import com.sanhai.live.util.ExpressionUtil;
import com.sanhai.live.util.ScreenSwitchUtils;
import com.zhangbin.paint.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class InputBarView extends LinearLayout implements View.OnClickListener, TextWatcher , OnExpressionListener {
    private EditText inputEdt;
    private TextView sendBtn;
    private ImageView expressionBtn;
    private RelativeLayout sendFlower;
    private TextView flowerNumTv;
    private ImageView flowerBtn;
    private InputMethodManager imm;
    private boolean isOpen = false;
    private ViewGroup.LayoutParams inputParams;
    private LinearLayout expressionLayout;
    private String sendContent;
    private boolean canSendFlower = true;
    private int flowerNum = 10;
    private boolean canInput = true;
    private Context mContext;
    private long preDismissTime = 0L;
    private int expressionAreaHeight = 150;
    public PopupWindow popupWindow;
    private OnSendFlowerListener sendFlowerListener;
    public InputBarView(Context context) {
        this(context, null);
        mContext = context;
    }

    public InputBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initEvent();
    }
    public void initView() {
        View view = View.inflate(getContext(), R.layout.edt, null);
        inputEdt = (EditText) view.findViewById(R.id.input_edt);
        sendBtn = (TextView) view.findViewById(R.id.send_btn);
        expressionBtn = (ImageView) view.findViewById(R.id.iv_expression);
        sendFlower = (RelativeLayout) view.findViewById(R.id.btn_send);
        flowerNumTv = (TextView) view.findViewById(R.id.flower_num);
        flowerBtn = (ImageView) view.findViewById(R.id.flower_btn);
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.addView(view);
        initExpressionPopupWindow();
    }
    /**
     * 初始化表情弹窗
     */
    public void initExpressionPopupWindow() {
        View view = View.inflate(getContext(),R.layout.popup_expression_layout, null);
        expressionLayout = (LinearLayout) view.findViewById(R.id.ll_expression_view_ipad);
        expressionLayout.setBackgroundResource(R.drawable.expresssion_bg);
        ExpressionView emotionView = new ExpressionView(getContext(), 8);
        emotionView.setOnEmotionSelectedListener(this);
        expressionLayout.addView(emotionView);
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, DimensionUtils.dip2px(getContext(), expressionAreaHeight));
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
    }
    public void initEvent() {
        sendBtn.setOnClickListener(this);
        expressionBtn.setOnClickListener(this);
        inputEdt.setOnClickListener(this);
        sendFlower.setOnClickListener(this);
        inputEdt.addTextChangedListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                preDismissTime = System.currentTimeMillis();
                if (!isOpen && ScreenSwitchUtils.getInstance(getContext()).isSensorSwitchLandScreen()) {
                    //switchInputAreaLength(false);
                }
            }
        });
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setSendStatus();
    }
    /**
     * 设置是否为发送状态
     */
    private void setSendStatus() {
        /*if (canSendFlower && TextUtils.isEmpty(inputEdt.getText().toString())) {
            flowerNumTv.setText(flowerNum + "");
            sendBtn.setVisibility(View.INVISIBLE);
            flowerBtn.setVisibility(View.VISIBLE);
            flowerNumTv.setVisibility(View.VISIBLE);
        } else {*/
            sendBtn.setVisibility(View.VISIBLE);
            flowerBtn.setVisibility(View.INVISIBLE);
            flowerNumTv.setVisibility(View.INVISIBLE);
            sendBtn.setText("发送");
        //}
    }
    /**
     * 是否显示非全屏表情
     */
    public void showOrCloseExpressionPopupWindow() {
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(this, 0, -this.getHeight() - DimensionUtils.dip2px(getContext(), expressionAreaHeight));
        } else {
            popupWindow.dismiss();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                if (!canInput)
                    return;
                sendContent = inputEdt.getText().toString().trim();
                /*if (!isOpen && ScreenSwitchUtils.getInstance(getContext()).isSensorSwitchLandScreen()) {
                    switchInputAreaLength(false);
                }*/
                EventBus.getDefault().post(sendContent);
                inputEdt.setText("");
                break;
            case R.id.iv_expression:
                if (!canInput) //禁言模式
                    return;
                showOrCloseExpressionPopupWindow();
                break;
            case R.id.btn_send:
                //送花按钮
                if (!canInput) //禁言模式
                    return;
                sendFlower();
                break;
        }
    }
    /**
     * 发送鲜花
     */
    private void sendFlower() {
        if (sendFlowerListener != null) {
            sendFlowerListener.onSendFlower();
        }
    }
    /**
     * 设置花的数目
     */
    public void setFlowerNum(int num) {
        flowerNum = num;
        flowerBtn.setSelected(num == 0);
        if (flowerNumTv != null)
            flowerNumTv.setText(flowerNum + "");
    }
    /**
     * 设置禁言或者解除
     */
    public void setGetDisableSend(int sendType, int resCode ,int forbidType, String userId, String userName, String userLoginId,String userLoginName,String time) {
       // Log.e("------", "sendType:" + sendType + " resCode:" + resCode + " forbidType:" + forbidType + "userId:" + userId + " userName:" + userName + " time:" + time);
        //sendType:1 resCode:0 forbidType:4userId: userName: time:
        if (resCode == 0) {//正常
            //解除
            if (sendType == 1) {
                if (forbidType == 4 || (forbidType ==3 && userLoginId.equals(userId) && userLoginName.equals(userName))) {
                    inputEdt.setEnabled(true);
                    inputEdt.setText("");
                    canInput = true;
                }
            } else { //禁言
                if (forbidType == 4 || (forbidType ==3 && userLoginId.equals(userId) && userLoginName.equals(userName))) {
                    inputEdt.setEnabled(false);
                    inputEdt.setText("你已被老师禁言！");
                    canInput = false;
                }
            }
        }else {//发送太快(1) 发送失败(2)

        }
    }

    @Override
    public void OnExpressionSelected(ExpressionEntity entity) {
        if (entity == null) return;
        //添加表情
        String content = inputEdt.getText().toString();
        content += entity.character;
        inputEdt.setText(ExpressionUtil.getExpressionString(getContext(),ExpressionView.map, content, "mipmap"));
        inputEdt.setSelection(content.length());
    }

    @Override
    public void OnExpressionRemove() {
        String content = inputEdt.getText().toString();
        if (!TextUtils.isEmpty(content)) {//非全屏删除
            int selectionStart = inputEdt.getSelectionStart();
            content = inputEdt.getText().delete(selectionStart - ExpressionUtil.dealContent(getContext(), content, selectionStart), selectionStart).toString();
            inputEdt.setText(ExpressionUtil.getExpressionString(getContext(), ExpressionView.map,content, "mipmap"));
            inputEdt.setSelection(content.length());
        }
    }
}
