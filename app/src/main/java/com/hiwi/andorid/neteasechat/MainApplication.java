package com.hiwi.andorid.neteasechat;

import android.app.Application;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NIMClient.init(this, loginInfo(), options());

        if (NIMUtil.isMainProcess(this)) {
            // 在主进程中初始化UI组件，判断所属进程方法请参见demo源码。
            initUiKit();
        }
    }

    private void initUiKit() {
        // 初始化 NimUIKit
        NimUIKit.init(this);

//        NimUIKit.setLocationProvider(new NimDemoLocationProvider());
    }


    private SDKOptions options() {
        return null;
    }

    private LoginInfo loginInfo() {
        return null;
    }
}