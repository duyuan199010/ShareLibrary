package com.dy.sharesdk.weixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dy.sharesdk.platform.IShareListener;
import com.dy.sharesdk.platform.ShareSdk;
import com.dy.sharesdk.platform.WxShareApi;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by duyuan797 on 16/3/30.
 */
public abstract class WxCallbackActivity extends Activity implements IWXAPIEventHandler {
    private IShareListener iShareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iShareListener = ShareSdk.getInstance().getiShareListener();
        WxShareApi.get().getApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        WxShareApi.get().getApi().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(iShareListener==null)
            return;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                iShareListener.onComplete();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                iShareListener.onCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                iShareListener.onError(null);
                break;
            default:
                break;
        }

        finish();
    }

}
