package com.dy.sharesdk.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by yangwen881 on 16/3/1.
 */
public class ToastUtils {
    private static Toast sToast;

    public static final void toastMsg(Context context, String msg) {
        if (context == null || TextUtils.isEmpty(msg)) {
            return;
        }
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        sToast.show();
    }

    public static final void toastMsg(Context context, int msgId) {
        toastMsg(context, context.getResources().getString(msgId));
    }
}
