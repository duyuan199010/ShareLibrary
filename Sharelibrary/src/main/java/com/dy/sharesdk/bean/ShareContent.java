package com.dy.sharesdk.bean;

import com.dy.sharesdk.media.BaseMediaObject;

/**
 * Created by duyuan797 on 16/3/22.
 */
public class ShareContent {

    private String mTitle;
    private String mSummary;
    private String mTargetUrl;
    private BaseMediaObject mMediaObject;

    public ShareContent(){}

    public ShareContent(String title, String summary, String targetUrl, BaseMediaObject media){
        this.mTitle = title;
        this.mSummary = summary;
        this.mTargetUrl = targetUrl;
        this.mMediaObject = media;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getmTargetUrl() {
        return mTargetUrl;
    }

    public void setmTargetUrl(String mTargetUrl) {
        this.mTargetUrl = mTargetUrl;
    }

    public BaseMediaObject getmMediaObject() {
        return mMediaObject;
    }

    public void setmMediaObject(BaseMediaObject mMediaObject) {
        this.mMediaObject = mMediaObject;
    }
}
