package com.dy.sharesdk.bean;

/**
 * Created by duyuan797 on 16/3/23.
 */
public class Platform {
    public static final int WEIXIN = 0;
    public static final int WEIXIN_CIRCLE = 1;
    public static final int WEIBO = 2;
    public static final int QQ = 3;
    public static final int QQ_ZONE = 4;
    public static final int MESSAGE = 5;

    private int mId;
    private String mName;
    private int mImgRes;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmImgRes() {
        return mImgRes;
    }

    public void setmImgRes(int mImgRes) {
        this.mImgRes = mImgRes;
    }
}
