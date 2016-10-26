package com.dy.sharesdk.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import com.dy.sharesdk.R;
import com.dy.sharesdk.adapter.ShareAdapter;

/**
 * Created by duyuan797 on 16/3/24.
 * 分享通用PopupWindow
 */
public class SharePopupWindow extends PopupWindow {
    private Activity mContext;

    public SharePopupWindow(final Activity context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.sdk_share, null);
        GridView gridView = (GridView) contentView.findViewById(R.id.gv_share);
        ShareAdapter shareAdapter = new ShareAdapter(context);
        gridView.setAdapter(shareAdapter);

        this.setContentView(contentView);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置背景,否则会有边框
        this.setBackgroundDrawable(new BitmapDrawable());

        this.update();
        this.setAnimationStyle(R.style.anim_menu_bottombar);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            setBackgroundAlpha(0.5f);
            this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void setBackgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }
}
