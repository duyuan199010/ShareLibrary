package com.dy.sharesdk.platform;


import com.dy.sharesdk.bean.ShareContent;

/**
 * Created by duyuan797 on 16/3/22.
 */
public interface IShareApi {
    void share(ShareContent shareContent, IShareListener listener);
}
