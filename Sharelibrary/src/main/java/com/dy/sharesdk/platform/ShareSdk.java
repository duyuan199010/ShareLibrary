package com.dy.sharesdk.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.dy.sharesdk.R;
import com.dy.sharesdk.bean.Platform;
import com.dy.sharesdk.bean.ShareContent;
import com.dy.sharesdk.utils.Consts;
import com.dy.sharesdk.utils.ToastUtils;
import com.dy.sharesdk.utils.Util;
import com.tencent.connect.common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duyuan797 on 16/3/22.
 */
public class ShareSdk {
    public static Context mContext;

    private BaseShareApi shareApi;
    private int mPlatform;
    private ShareContent mShareContent;
    private IShareListener iShareListener;
    private List<Platform> mPlatformList;

    private static ShareSdk instance;

    public static void init(Context context){
        mContext = context;
    }

    private ShareSdk() {
        initPlatform();
    }

    public static ShareSdk getInstance() {
        if (instance == null) {
            synchronized (ShareSdk.class) {
                if (instance == null) {
                    instance = new ShareSdk();
                }
            }
        }
        return instance;
    }

    public void share(Activity activity) {
        switch (mPlatform) {
            case Platform.WEIXIN:
                if (Util.isInstall(mContext, Consts.WEIXIN_PKG)) {
                    shareApi = WxShareApi.get();
                    shareApi.setSharePlatform(Platform.WEIXIN);
                } else {
                    ToastUtils.toastMsg(mContext,
                            mContext.getResources().getString(R.string.install_weixin));
                    return;
                }
                break;
            case Platform.WEIXIN_CIRCLE:
                if (Util.isInstall(mContext, Consts.WEIXIN_PKG)) {
                    shareApi = WxShareApi.get();
                    shareApi.setSharePlatform(Platform.WEIXIN_CIRCLE);
                } else {
                    ToastUtils.toastMsg(mContext,
                            mContext.getResources().getString(R.string.install_weixin));
                    return;
                }
                break;
            case Platform.WEIBO:
                shareApi = WeiboShareApi.get(activity);

                break;
            case Platform.QQ:
                if (Util.isInstall(mContext, Consts.QQ_PKG)) {
                    shareApi = QQShareApi.get(activity);
                    shareApi.setSharePlatform(Platform.QQ);
                } else {
                    ToastUtils.toastMsg(mContext,
                            mContext.getResources().getString(R.string.install_qq));
                    return;
                }
                break;
            case Platform.QQ_ZONE:
                if (Util.isInstall(mContext, Consts.QQ_PKG)) {
                    shareApi = QQShareApi.get(activity);
                    shareApi.setSharePlatform(Platform.QQ_ZONE);
                } else {
                    ToastUtils.toastMsg(mContext,
                            mContext.getResources().getString(R.string.install_qq));
                    return;
                }
                break;
            case Platform.MESSAGE:
                sendSMS(Consts.DOWNLOAD_URL);
                return;
        }
        shareApi.share(mShareContent, iShareListener);
    }

    private void sendSMS(String webUrl) {
        String smsBody = "通过短信分享的内容" + webUrl;
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //短信内容
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(sendIntent);
    }

    private void initPlatform() {
        mPlatformList = new ArrayList<>();

        for (int i = 0; i < Consts.SUPPORT_SHARE_PLATFORM.length; i++) {
            Platform p = new Platform();
            p.setmId(Consts.SUPPORT_SHARE_PLATFORM[i]);
            if (Consts.SUPPORT_SHARE_PLATFORM[i] == Platform.WEIXIN) {
                p.setmName(mContext.getResources().getString(R.string.share_wechat));
                p.setmImgRes(R.mipmap.share_wechat);
                mPlatformList.add(p);
                continue;
            }
            if (Consts.SUPPORT_SHARE_PLATFORM[i] == Platform.WEIXIN_CIRCLE) {
                p.setmName(mContext.getResources().getString(R.string.share_wechatcircle));
                p.setmImgRes(R.mipmap.share_wechatcircle);
                mPlatformList.add(p);
                continue;
            }
            if (Consts.SUPPORT_SHARE_PLATFORM[i] == Platform.WEIBO) {
                p.setmName(mContext.getResources().getString(R.string.share_weibo));
                p.setmImgRes(R.mipmap.share_weibo);
                mPlatformList.add(p);
                continue;
            }
            if (Consts.SUPPORT_SHARE_PLATFORM[i] == Platform.QQ) {
                p.setmName(mContext.getResources().getString(R.string.share_qq));
                p.setmImgRes(R.mipmap.share_qq);
                mPlatformList.add(p);
                continue;
            }
            if (Consts.SUPPORT_SHARE_PLATFORM[i] == Platform.QQ_ZONE) {
                p.setmName(mContext.getResources().getString(R.string.share_qzone));
                p.setmImgRes(R.mipmap.share_qzone);
                mPlatformList.add(p);
                continue;
            }
            if (Consts.SUPPORT_SHARE_PLATFORM[i] == Platform.MESSAGE) {
                p.setmName(mContext.getResources().getString(R.string.message));
                p.setmImgRes(R.mipmap.share_message);
                mPlatformList.add(p);
                continue;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE
                || requestCode == Constants.REQUEST_QZONE_SHARE) {

            if (shareApi != null) {
                shareApi.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void setCallback(IShareListener listener) {
        this.iShareListener = listener;
    }

    public void setPlatform(int platform) {
        this.mPlatform = platform;
    }

    public void setmShareContent(ShareContent mShareContent) {
        this.mShareContent = mShareContent;
    }

    public IShareListener getiShareListener() {
        return iShareListener;
    }

    public List<Platform> getPlatformList() {
        return mPlatformList;
    }
}
