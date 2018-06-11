package com.hiwi.andorid.neteasechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NimUIKit.login(new LoginInfo("qaul11111", "82480c6d75bf70b8c962625b3b153737"), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                Log.e(TAG, "onSuccess: ");
                NimUIKit.startP2PSession(NimUIKit.getContext(), "qaul22222");
            }

            @Override
            public void onFailed(int i) {
                Log.e(TAG, "onFailed: " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG, "onException: ", throwable);
            }
        });
    }
}
