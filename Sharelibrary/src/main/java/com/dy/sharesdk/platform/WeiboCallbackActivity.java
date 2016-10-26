package com.dy.sharesdk.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dy.sharesdk.utils.Consts;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by duyuan797 on 16/3/22.
 * 接收微博分享结果
 */
public class WeiboCallbackActivity extends Activity implements IWeiboHandler.Response{

    private Activity mContext;
    private IWeiboShareAPI api;

    public static IShareListener iShareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            api = WeiboShareSDK.createWeiboAPI(this, Consts.WEIBO_APPID);
            api.registerApp();
            Intent intent = getIntent();
            api.handleWeiboResponse(intent, this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                iShareListener.onComplete();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                iShareListener.onCancel();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                iShareListener.onError(null);
                break;
        }

        finish();
    }
}
