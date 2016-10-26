package com.dy.sharesdk.media;

import android.graphics.Bitmap;

/**
 * Created by duyuan797 on 16/3/22.
 */
public abstract class BaseMediaObject {
    protected String mText;
    protected String mUrl;
    protected Bitmap mBitmap;
    protected String mPath;
    protected Bitmap mThumbBitmap;

    public void setmThumbBitmap(Bitmap bitmap){
        this.mThumbBitmap = bitmap;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public Bitmap getmThumbBitmap() {
        return mThumbBitmap;
    }
}
