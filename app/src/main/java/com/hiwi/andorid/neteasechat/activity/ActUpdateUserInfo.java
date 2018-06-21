package com.hiwi.andorid.neteasechat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.util.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

public class ActUpdateUserInfo extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActUpdateUserInfo";

    private String title;
    private EditText editText;

    public static void start(Context context, String title, String content) {
        Intent intent = new Intent(context, ActUpdateUserInfo.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_update_user_info);

        title = getIntent().getStringExtra("title");
        setTitle(title + "修改");

        editText = findViewById(R.id.edit_update_user_info);
        Button btnUpdate = findViewById(R.id.btn_update_user_info);

        btnUpdate.setOnClickListener(this);

        editText.setText(getIntent().getStringExtra("content"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_user_info:
                updateUserInfo();
                break;
        }
    }

    private void updateUserInfo() {
        String content = editText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfoFieldEnum userInfoFieldEnum = null;
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        switch (title) {
            case "nickname":
                userInfoFieldEnum = UserInfoFieldEnum.Name;
                break;
            case "gender":
                int gender;
                userInfoFieldEnum = UserInfoFieldEnum.GENDER;
                if (content.equals("男")) {
                    gender = GenderEnum.MALE.getValue();
                } else if (content.equals("女")) {
                    gender = GenderEnum.FEMALE.getValue();
                } else {
                    gender = GenderEnum.UNKNOWN.getValue();
                }
                fields.put(userInfoFieldEnum, gender);
                break;
            case "birth":
                userInfoFieldEnum = UserInfoFieldEnum.BIRTHDAY;
                break;
            case "tel":
                userInfoFieldEnum = UserInfoFieldEnum.MOBILE;
                break;
            case "mail":
                userInfoFieldEnum = UserInfoFieldEnum.EMAIL;
                break;
            case "signature":
                userInfoFieldEnum = UserInfoFieldEnum.SIGNATURE;
                break;
            default:
                break;
        }

        if (!title.equals("gender")) {
            fields.put(userInfoFieldEnum, content);
        }
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActUpdateUserInfo.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {
                        Toast.makeText(ActUpdateUserInfo.this, "修改失败", Toast.LENGTH_SHORT).show();
                        LogUtil.e(TAG, "修改失败信息: " + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        LogUtil.w(TAG, "修改异常信息: " + throwable.getMessage());
                    }
                });
    }
}
