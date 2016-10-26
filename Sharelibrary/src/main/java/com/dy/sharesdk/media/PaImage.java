package com.dy.sharesdk.media;

import android.graphics.Bitmap;
import java.io.File;

/**
 * Created by duyuan797 on 16/3/22.
 */
public class PaImage extends BaseMediaObject {

    public PaImage(Bitmap bitmap, String url){
        mBitmap = bitmap;
        mUrl = url;
    }

    public PaImage(String url){
        mUrl = url;
    }

    public PaImage(Bitmap bitmap){
        mBitmap = bitmap;
    }

    public PaImage(File file){
        mPath = file.getPath();
    }

}
