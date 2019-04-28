package com.example.lijian.sf_im_sdk;

public interface OnUpdateUiInterface {

    public abstract void UpdateUIInterface(MsgContent msg);

    public abstract void UpdateLoginInterface(int state);

    public abstract void UpdateSendMsgDataStateInterface(int resCode, String userID);
}
