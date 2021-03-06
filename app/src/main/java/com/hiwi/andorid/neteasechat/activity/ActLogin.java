package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.ContactHttpClient;
import com.hiwi.andorid.neteasechat.DemoCache;
import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.config.preference.Preferences;
import com.hiwi.andorid.neteasechat.config.preference.UserPreferences;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class ActLogin extends Activity {
    private static final String TAG = "ActLogin";
    private EditText ed1;
    private EditText ed2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed1 = findViewById(R.id.act_login_name);
        ed2 = findViewById(R.id.act_login_pwd);
        Button btn = findViewById(R.id.act_login_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed1.getText().toString();
                String pwd = ed2.getText().toString();
//                ("qaul11111", "82480c6d75bf70b8c962625b3b153737"),
                NimUIKit.login(new LoginInfo("qaul11111", "82480c6d75bf70b8c962625b3b153737"), new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        DemoCache.setAccount("qaul11111");
                        saveLoginInfo("qaul11111", "82480c6d75bf70b8c962625b3b153737");
                        Log.e(TAG, "onSuccess: ");

                        // 初始化消息提醒配置
                        initNotificationConfig();

                        Toast.makeText(ActLogin.this,"登录成功",Toast.LENGTH_LONG).show();
                        ActLogin.this.finish();
//                        NimUIKit.startP2PSession(NimUIKit.getContext(), "qaul22222");
                    }

                    @Override
                    public void onFailed(int i) {
                        Log.e(TAG, "onFailed: " + i);
                        Toast.makeText(ActLogin.this,"登录成功",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Log.e(TAG, "onException: ", throwable);
                        Toast.makeText(ActLogin.this,"登录成功",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(0,R.anim.out);
        return super.onKeyDown(keyCode, event);
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }


    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }
}
