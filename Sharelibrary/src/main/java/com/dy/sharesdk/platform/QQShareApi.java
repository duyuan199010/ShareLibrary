package com.dy.sharesdk.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.dy.sharesdk.R;
import com.dy.sharesdk.bean.Platform;
import com.dy.sharesdk.bean.ShareContent;
import com.dy.sharesdk.media.BaseMediaObject;
import com.dy.sharesdk.media.PaImage;
import com.dy.sharesdk.media.PaMusic;
import com.dy.sharesdk.media.PaText;
import com.dy.sharesdk.media.PaVideo;
import com.dy.sharesdk.media.PaWebpage;
import com.dy.sharesdk.utils.Consts;
import com.dy.sharesdk.utils.ToastUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.util.ArrayList;

/**
 * Created by duyuan797 on 16/3/22.
 */
public class QQShareApi extends BaseShareApi {
    private static QQShareApi qqShareApi;
    private Context mContext;
    private Activity mActivity;
    private Tencent api;
    private IShareListener iShareListener;
    private int mPlatform;

    private final String LOGO_URL =
            "https://mmbiz.qlogo.cn/mmbiz/C1AQR9LJAEmayX4rfPJKYe0hKZkLSjZgcCfO5QX6jTlDN2S"
                    + "biapOEvZo6NKG9iaicbksdgOxtxb8fGYsdfGs7ibV7A/0?wx_fmt=png";

    private QQShareApi(Activity activity) {
        this.mContext = ShareSdk.mContext;
        this.mActivity = activity;
        api = Tencent.createInstance(Consts.QQ_APPID, mContext);
    }

    public static QQShareApi get(Activity activity) {
        if (qqShareApi == null) {
            synchronized (QQShareApi.class) {
                if (qqShareApi == null) {
                    qqShareApi = new QQShareApi(activity);
                }
            }
        }
        return qqShareApi;
    }

    @Override public void setSharePlatform(int platform) {
        this.mPlatform = platform;
    }

    @Override public void share(ShareContent shareContent, IShareListener listener) {
        if (shareContent == null || shareContent.getmMediaObject() == null) {
            return;
        }
        iShareListener = listener;

        if (mPlatform == Platform.QQ) {
            doShareToQQ(shareContent);
        } else if (mPlatform == Platform.QQ_ZONE) {
            doShareToQQZone(shareContent);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }

    private void doShareToQQ(ShareContent shareContent) {
        BaseMediaObject mediaObject = shareContent.getmMediaObject();
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getmTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getmSummary());
        if (mediaObject instanceof PaText) {
            shareText(mediaObject.getmText());
            return;
        } else if (mediaObject instanceof PaImage) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            ArrayList<String> imageUrls = new ArrayList<String>();
            imageUrls.add(mediaObject.getmUrl());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mediaObject.getmUrl());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getmTargetUrl());
        } else if (mediaObject instanceof PaMusic) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, mediaObject.getmUrl());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getmTargetUrl());
        } else if (mediaObject instanceof PaVideo) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, mediaObject.getmUrl());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getmTargetUrl());
        } else if (mediaObject instanceof PaWebpage) {
            ArrayList<String> imageUrls = new ArrayList<String>();
            imageUrls.add(LOGO_URL);
            //params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls.get(0));
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getmTargetUrl());
        } else {
            ToastUtils.toastMsg(mContext, mContext.getResources().getString(R.string.unsupport));
            return;
        }

        ThreadManager.getMainHandler().post(new Runnable() {
            @Override public void run() {
                if (null != api) {
                    api.shareToQQ(mActivity, params, qqShareListener);
                }
            }
        });
    }

    private void doShareToQQZone(ShareContent shareContent) {
        BaseMediaObject mediaObject = shareContent.getmMediaObject();
        final Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getmTitle());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.getmSummary());

        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (mediaObject.getmUrl() == null) {
            mediaObject.setmUrl(LOGO_URL);
        }


        imageUrls.add(LOGO_URL);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.getmTargetUrl());

        ThreadManager.getMainHandler().post(new Runnable() {

            @Override public void run() {
                if (null != api) {
                    api.shareToQzone(mActivity, params, qqShareListener);
                }
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override public void onCancel() {
            iShareListener.onCancel();
        }

        @Override public void onComplete(Object response) {
            iShareListener.onComplete();
        }

        @Override public void onError(UiError e) {
            iShareListener.onError(null);
        }
    };

    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        try {
            sendIntent.setClassName("com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.JumpActivity");
            mContext.startActivity(sendIntent);
        } catch (Exception e) {
        }
    }
}
