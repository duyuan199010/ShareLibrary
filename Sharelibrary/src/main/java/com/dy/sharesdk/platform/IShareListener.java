package com.dy.sharesdk.platform;

/**
 * Created by duyuan797 on 16/3/22.
 */
public interface IShareListener {
    void onCancel();
    void onComplete();
    void onError(String error);
}
