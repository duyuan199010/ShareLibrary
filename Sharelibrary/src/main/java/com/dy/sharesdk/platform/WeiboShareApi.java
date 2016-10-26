package com.dy.sharesdk.platform;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.dy.sharesdk.R;
import com.dy.sharesdk.bean.ShareContent;
import com.dy.sharesdk.media.BaseMediaObject;
import com.dy.sharesdk.media.PaImage;
import com.dy.sharesdk.media.PaMusic;
import com.dy.sharesdk.media.PaText;
import com.dy.sharesdk.media.PaVideo;
import com.dy.sharesdk.media.PaWebpage;
import com.dy.sharesdk.utils.Consts;
import com.dy.sharesdk.utils.ToastUtils;
import com.dy.sharesdk.utils.Util;
import com.dy.sharesdk.weibo.AccessTokenKeeper;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;

/**
 * Created by duyuan797 on 16/3/22.
 */
public class WeiboShareApi extends BaseShareApi {
    private static WeiboShareApi weiboShareApi;

    private Context mContext;
    private Activity mActivity;
    private IWeiboShareAPI api;

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private ShareContent mShareContent;

    private WeiboShareApi(Activity activity) {
        this.mContext = ShareSdk.mContext;
        this.mActivity = activity;
        api = WeiboShareSDK.createWeiboAPI(mContext, Consts.WEIBO_APPID);
        api.registerApp();
    }

    public static WeiboShareApi get(Activity activity) {
        if (weiboShareApi == null) {
            synchronized (WeiboShareApi.class) {
                if (weiboShareApi == null) {
                    weiboShareApi = new WeiboShareApi(activity);
                }
            }
        }
        return weiboShareApi;
    }

    @Override public void share(ShareContent shareContent, IShareListener listener) {
        if (shareContent == null || shareContent.getmMediaObject() == null) {
            return;
        }
        WeiboCallbackActivity.iShareListener = listener;

        if (Util.isInstall(mContext, Consts.WEIBO_PKG)) {
            shareByClient(shareContent);
        } else {
            shareByWeb(shareContent);
        }
    }

    private void shareByClient(ShareContent shareContent) {
        BaseMediaObject mediaObject = shareContent.getmMediaObject();
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        WeiboMessage msg = new WeiboMessage();

        if (mediaObject instanceof PaText) {
            TextObject textObject = new TextObject();
            textObject.title = shareContent.getmTitle();
            textObject.description = shareContent.getmSummary();
            textObject.text = mediaObject.getmText();
            msg.mediaObject = textObject;
            request.transaction = "text";
        } else if (mediaObject instanceof PaImage) {
            ImageObject imageObject = new ImageObject();
            imageObject.title = shareContent.getmTitle();
            imageObject.description = shareContent.getmSummary();
            imageObject.identify = "identify";
            if (mediaObject.getmBitmap() != null) {
                imageObject.setImageObject(mediaObject.getmBitmap());
            } else if (mediaObject.getmUrl() != null) {
                imageObject.actionUrl = mediaObject.getmUrl();
            }

            msg.mediaObject = imageObject;
            request.transaction = "img";
        } else if (mediaObject instanceof PaMusic) {
            MusicObject musicObject = new MusicObject();
            //identify为必填项
            musicObject.identify = "identify";
            musicObject.title = shareContent.getmTitle();
            musicObject.description = shareContent.getmSummary();
            musicObject.actionUrl = mediaObject.getmUrl();
            //musicObject.dataUrl = "www.weibo.com";
            //musicObject.dataHdUrl = "www.weibo.com";
            musicObject.duration = 10;
            musicObject.defaultText = "Music 默认文案";
            musicObject.setThumbImage(mediaObject.getmThumbBitmap());
            msg.mediaObject = musicObject;
            request.transaction = "music";
        } else if (mediaObject instanceof PaVideo) {
            VideoObject videoObject = new VideoObject();
            videoObject.identify = "identify";
            videoObject.title = shareContent.getmTitle();
            videoObject.description = shareContent.getmSummary();
            videoObject.actionUrl = mediaObject.getmUrl();
            videoObject.dataUrl = "www.weibo.com";
            videoObject.dataHdUrl = "www.weibo.com";
            videoObject.duration = 10;
            videoObject.defaultText = "Vedio 默认文案";
            videoObject.setThumbImage(mediaObject.getmThumbBitmap());
            msg.mediaObject = videoObject;
            request.transaction = "video";
        } else if (mediaObject instanceof PaWebpage) {
            WebpageObject webpageObject = new WebpageObject();
            webpageObject.identify = "identify";
            webpageObject.title = shareContent.getmTitle();
            webpageObject.description = shareContent.getmSummary();
            webpageObject.actionUrl = mediaObject.getmUrl();
            webpageObject.setThumbImage(mediaObject.getmThumbBitmap());
            msg.mediaObject = webpageObject;
            request.transaction = "webpage";
        }

        request.message = msg;
        api.sendRequest(mActivity, request);
    }

    private void shareByWeb(ShareContent shareContent) {
        //mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        if (mAccessToken == null || !mAccessToken.isSessionValid()) {
            auth();
            this.mShareContent = shareContent;
        } else {
            shareByOpenApi(shareContent);
        }
    }

    private void auth() {
        mAccessToken = new Oauth2AccessToken();
        mAuthInfo = new AuthInfo(mContext, Consts.WEIBO_APPID, Consts.REDIRECT_URL, Consts.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
        mSsoHandler.authorizeWeb(authListener);
    }

    private void shareByOpenApi(ShareContent shareContent) {
        StatusesAPI mStatusesAPI = new StatusesAPI(mContext, Consts.WEIBO_APPID, mAccessToken);
        BaseMediaObject mediaObject = shareContent.getmMediaObject();
        if (mediaObject instanceof PaText) {
            mStatusesAPI.update(mediaObject.getmText(), null, null, requestListener);
        } else if (mediaObject instanceof PaImage) {
            if (mediaObject.getmBitmap() != null) {
                mStatusesAPI.upload(shareContent.getmTitle(), mediaObject.getmBitmap(), null, null,
                        requestListener);
            } else if (mediaObject.getmUrl() != null) {
                //高级权限,应用审核通过后才能使用
                mStatusesAPI.uploadUrlText(shareContent.getmTitle(), mediaObject.getmUrl(), "11",
                        null, null, requestListener);
            }
        } else {
            ToastUtils.toastMsg(mContext,
                    mContext.getResources().getString(R.string.install_weibo));
        }
    }

    private RequestListener requestListener = new RequestListener() {
        @Override public void onComplete(String response) {
            WeiboCallbackActivity.iShareListener.onComplete();
        }

        @Override public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            WeiboCallbackActivity.iShareListener.onError(info.toString());
        }
    };

    private WeiboAuthListener authListener = new WeiboAuthListener() {
        @Override public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
                Toast.makeText(mContext, mContext.getResources().getString(R.string.auth_success),
                        Toast.LENGTH_SHORT).show();

                shareByOpenApi(mShareContent);
            } else {
                String code = values.getString("code");
                String message = mContext.getString(R.string.auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                ToastUtils.toastMsg(mContext, message);
            }
        }

        @Override public void onCancel() {
            ToastUtils.toastMsg(mContext, mContext.getResources().getString(R.string.auth_cancel));
        }

        @Override public void onWeiboException(WeiboException e) {
            ToastUtils.toastMsg(mContext, mContext.getResources().getString(R.string.auth_error));
        }
    };
}
