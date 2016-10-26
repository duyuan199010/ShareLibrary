package com.dy.sharesdk.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dy.sharesdk.R;
import com.dy.sharesdk.bean.Platform;
import com.dy.sharesdk.platform.ShareSdk;

/**
 * Created by duyuan797 on 16/3/24.
 */
public class ShareAdapter extends BaseAdapter{
    private Activity mContext;

    public ShareAdapter(Activity context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return ShareSdk.getInstance().getPlatformList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Platform platform = ShareSdk.getInstance().getPlatformList().get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sdk_share_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_share);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_share);
            viewHolder.textView.setText(platform.getmName());
            viewHolder.imageView.setImageResource(platform.getmImgRes());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.textView.setText(platform.getmName());
            viewHolder.imageView.setImageResource(platform.getmImgRes());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (platform.getmId()) {
                    case Platform.WEIXIN:
                        ShareSdk.getInstance().setPlatform(Platform.WEIXIN);
                        break;
                    case Platform.WEIXIN_CIRCLE:
                        ShareSdk.getInstance().setPlatform(Platform.WEIXIN_CIRCLE);
                        break;
                    case Platform.QQ:
                        ShareSdk.getInstance().setPlatform(Platform.QQ);
                        break;
                    case Platform.QQ_ZONE:
                        ShareSdk.getInstance().setPlatform(Platform.QQ_ZONE);
                        break;
                    case Platform.WEIBO:
                        ShareSdk.getInstance().setPlatform(Platform.WEIBO);
                        break;
                    case Platform.MESSAGE:
                        ShareSdk.getInstance().setPlatform(Platform.MESSAGE);
                        break;
                }
                ShareSdk.getInstance().share(mContext);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

}
