package com.dy.sharesdk.utils;

import com.dy.sharesdk.bean.Platform;

/**
 * Created by duyuan797 on 16/3/21.
 */
public class Consts {

    public static final String WX_APPID = "wx8df5e14110ac3794";
    //public static final String WX_APPID = "wx018ff549e3992faf";
    public static final String WEIBO_APPID = "2852847070";
    public static final String QQ_APPID = "1105649392";

    public static final String DOWNLOAD_URL = "http://android.myapp.com/";

    //设置支持分享的第三方平台
    public static final int[] SUPPORT_SHARE_PLATFORM = {
            Platform.WEIXIN, Platform.WEIXIN_CIRCLE,
            Platform.WEIBO, Platform.QQ_ZONE, Platform.QQ, Platform.MESSAGE};

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p/>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p/>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String WEIXIN_PKG = "com.tencent.mm";
    public static final String WEIBO_PKG = "com.sina.weibo";
    public static final String QQ_PKG = "com.tencent.mobileqq";
}
