package com.dy.sharesdk.platform;

import android.content.Intent;
import com.dy.sharesdk.bean.ShareContent;

/**
 * Created by duyuan797 on 16/3/31.
 */
public class BaseShareApi implements IShareApi {
    @Override
    public void share(ShareContent shareContent, IShareListener listener) {

    }

    public void setSharePlatform(int platform){}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
