package com.dy.sharesdk.platform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.dy.sharesdk.utils.Util;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * Created by duyuan797 on 16/3/22.
 */
public class WxShareApi extends BaseShareApi {
    private static WxShareApi wxShareApi;
    private Context mContext;
    private IWXAPI api;
    private int mPlatform;

    private WxShareApi() {
        this.mContext = ShareSdk.mContext;
        api = WXAPIFactory.createWXAPI(mContext, Consts.WX_APPID);
        api.registerApp(Consts.WX_APPID);
    }

    public static WxShareApi get() {
        if (wxShareApi == null) {
            synchronized (WxShareApi.class) {
                if (wxShareApi == null) {
                    wxShareApi = new WxShareApi();
                }
            }
        }
        return wxShareApi;
    }

    public IWXAPI getApi() {

        return api;
    }

    @Override public void setSharePlatform(int platform) {
        this.mPlatform = platform;
    }

    @Override public void share(ShareContent shareContent, IShareListener listener) {
        if (shareContent == null || shareContent.getmMediaObject() == null) {
            return;
        }
        BaseMediaObject mediaObject = shareContent.getmMediaObject();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXMediaMessage msg = null;

        if (mediaObject instanceof PaText) {
            WXTextObject textObject = new WXTextObject(mediaObject.getmText());
            msg = new WXMediaMessage(textObject);
            msg.description = "text description";
            req.transaction = "text";
        } else if (mediaObject instanceof PaImage) {
            WXImageObject imageObject = new WXImageObject();
            if (mediaObject.getmBitmap() != null) {
                imageObject = new WXImageObject(mediaObject.getmBitmap());
            } else if (mediaObject.getmUrl() != null) {
                imageObject.imagePath = mediaObject.getmUrl();
            } else if (mediaObject.getmPath() != null) {
                imageObject.imagePath = mediaObject.getmPath();
            }
            msg = new WXMediaMessage(imageObject);
            msg.setThumbImage(mediaObject.getmThumbBitmap());
            msg.title = shareContent.getmTitle();
            msg.description = shareContent.getmSummary();
            req.transaction = "img";
        } else if (mediaObject instanceof PaMusic) {
            WXMusicObject musicObject = new WXMusicObject();
            musicObject.musicUrl = mediaObject.getmUrl();
            msg = new WXMediaMessage(musicObject);
            msg.setThumbImage(mediaObject.getmThumbBitmap());
            msg.title = shareContent.getmTitle();
            msg.description = shareContent.getmSummary();
            req.transaction = "music";
        } else if (mediaObject instanceof PaVideo) {
            WXVideoObject videoObject = new WXVideoObject();
            videoObject.videoUrl = mediaObject.getmUrl();
            msg = new WXMediaMessage(videoObject);
            msg.title = shareContent.getmTitle();
            msg.description = shareContent.getmSummary();
            req.transaction = "video";
        } else if (mediaObject instanceof PaWebpage) {
            WXWebpageObject webpageObject = new WXWebpageObject();
            webpageObject.webpageUrl = mediaObject.getmUrl();
            msg = new WXMediaMessage(webpageObject);
            msg.title = shareContent.getmTitle();
            msg.description = shareContent.getmSummary();
            req.transaction = "webpage";
        }
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        req.message = msg;
        req.scene = mPlatform == Platform.WEIXIN_CIRCLE ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }
}
