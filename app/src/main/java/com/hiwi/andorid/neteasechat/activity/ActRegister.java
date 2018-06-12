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
import com.netease.nimlib.sdk.auth.LoginInfo;

import static com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener.TAG;

public class ActRegister extends Activity {

    private EditText ed1;
    private EditText ed2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed1 = findViewById(R.id.act_register_name);
        ed2 = findViewById(R.id.act_register_pwd);
        Button btn = findViewById(R.id.act_register_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed1.getText().toString();
                String pwd = ed2.getText().toString();
                DialogMaker.showProgressDialog(ActRegister.this, "注册中", false);
                ContactHttpClient.getInstance().register("124565sd1fasdf", "8645ertdf", "1234567890", new ContactHttpClient.ContactHttpCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActRegister.this, "注册中", Toast.LENGTH_SHORT).show();
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onFailed(int code, String errorMsg) {
                        Toast.makeText(ActRegister.this, "注册失败", Toast.LENGTH_SHORT)
                                .show();

                        DialogMaker.dismissProgressDialog();
                    }
                });
            }
        });
    }
}
