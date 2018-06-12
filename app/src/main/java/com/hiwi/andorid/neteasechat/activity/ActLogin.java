package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.ContactHttpClient;
import com.hiwi.andorid.neteasechat.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.RequestCallback;
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

                NimUIKit.login(new LoginInfo("qaul11111", "82480c6d75bf70b8c962625b3b153737"), new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        Log.e(TAG, "onSuccess: ");
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
}
