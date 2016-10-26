package share.dy.com.sharedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.dy.sharesdk.bean.ShareContent;
import com.dy.sharesdk.media.PaWebpage;
import com.dy.sharesdk.platform.IShareListener;
import com.dy.sharesdk.platform.ShareSdk;
import com.dy.sharesdk.view.SharePopupWindow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShareSdk.init(getApplicationContext());
        ShareSdk.getInstance().share(this);
        ShareSdk.getInstance().setCallback(listener);
        final SharePopupWindow popupWindow = new SharePopupWindow(this);

        PaWebpage paWebpage = new PaWebpage("http:www.baidu.com");
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        paWebpage.setmThumbBitmap(thumbBmp);
        ShareContent mShareContent = new ShareContent("分享的内容", "分享的标题", "分享的URL",
                paWebpage);
        //4.设置分享的内容
        ShareSdk.getInstance().setmShareContent(mShareContent);

        final TextView textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                popupWindow.showPopupWindow(textView);
            }
        });
    }


    IShareListener listener = new IShareListener() {
        @Override public void onCancel() {
            Toast.makeText(MainActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
        }

        @Override public void onComplete() {
            Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override public void onError(String error) {
            Toast.makeText(MainActivity.this, "分享失败: " + error, Toast.LENGTH_SHORT).show();
        }
    };
}
