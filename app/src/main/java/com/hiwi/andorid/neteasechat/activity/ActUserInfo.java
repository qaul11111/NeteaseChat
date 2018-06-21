package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.helper.UserUpdateHelper;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.File;

public class ActUserInfo extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActUserInfo";

    // constant
    private static final int PICK_AVATAR_REQUEST = 0x0E;
    private static final int AVATAR_TIME_OUT = 30000;

    // data
    AbortableFuture<String> uploadAvatarFuture;

    private String account;
    private NimUserInfo user;

    private ImageView imgHead;
    private TextView txtNickName;
    private TextView txtGender;
    private TextView txtBirth;
    private TextView txtTelephone;
    private TextView txtMail;
    private TextView txtSignature;

    /**
     * 传入用户 id
     *
     * @param context
     * @param account
     */
    public static void start(Context context, String account) {
        Intent intent = new Intent(context, ActUserInfo.class);
        intent.putExtra("account", account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_user_info);
        account = getIntent().getStringExtra("account");

        findView();
        initUserInfo();
        initListener();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        user = NIMClient.getService(UserService.class).getUserInfo(account);
        initView();
    }

    private void initListener() {
        imgHead.setOnClickListener(this);
        txtNickName.setOnClickListener(this);
        txtGender.setOnClickListener(this);
        txtBirth.setOnClickListener(this);
        txtTelephone.setOnClickListener(this);
        txtMail.setOnClickListener(this);
        txtSignature.setOnClickListener(this);
    }

    private void findView() {
        imgHead = findViewById(R.id.img_user_info_head);
        txtNickName = findViewById(R.id.txt_user_info_nickname);
        txtGender = findViewById(R.id.txt_user_info_gender);
        txtBirth = findViewById(R.id.txt_user_info_birth);
        txtTelephone = findViewById(R.id.txt_user_info_tel);
        txtMail = findViewById(R.id.txt_user_info_mail);
        txtSignature = findViewById(R.id.txt_user_info_signature);
    }

    private void initView() {
        Glide.with(this).load(user.getAvatar()).into(imgHead);
        txtNickName.setText(user.getName());
        int value = user.getGenderEnum().getValue();
        String gender = null;
        switch (value) {
            case 1:
                gender = "男";
                break;
            case 2:
                gender = "女";
                break;
            case 0:
                gender = "未知";
                break;
        }
        txtGender.setText(gender);
        txtBirth.setText(user.getBirthday());
        txtTelephone.setText(user.getMobile());
        txtMail.setText(user.getEmail());
        txtSignature.setText(user.getSignature());
    }

    private void initUserInfo() {
        user = NIMClient.getService(UserService.class).getUserInfo(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_user_info_head:
                PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
                option.titleResId = R.string.set_head_image;
                option.crop = true;
                option.multiSelect = false;
                option.cropOutputImageWidth = 720;
                option.cropOutputImageHeight = 720;
                PickImageHelper.pickImage(this, PICK_AVATAR_REQUEST, option);
                break;
            case R.id.txt_user_info_nickname:
                ActUpdateUserInfo.start(this, "nickname", txtNickName.getText().toString());
                break;
            case R.id.txt_user_info_gender:
                ActUpdateUserInfo.start(this, "gender", txtGender.getText().toString());
                break;
            case R.id.txt_user_info_birth:
                ActUpdateUserInfo.start(this, "birth", txtBirth.getText().toString());
                break;
            case R.id.txt_user_info_tel:
                ActUpdateUserInfo.start(this, "tel", txtTelephone.getText().toString());
                break;
            case R.id.txt_user_info_mail:
                ActUpdateUserInfo.start(this, "mail", txtMail.getText().toString());
                break;
            case R.id.txt_user_info_signature:
                ActUpdateUserInfo.start(this, "signature", txtSignature.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
            String path = data.getStringExtra(com.netease.nim.uikit.business.session.constant.Extras.EXTRA_FILE_PATH);
            updateAvatar(path);
        }
    }

    /**
     * 更新头像
     */
    private void updateAvatar(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file == null) {
            return;
        }

        DialogMaker.showProgressDialog(this, null, null, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelUpload(R.string.user_info_update_cancel);
            }
        }).setCanceledOnTouchOutside(true);

        LogUtil.i(TAG, "start upload avatar, local file path=" + file.getAbsolutePath());
        new Handler().postDelayed(outimeTask, AVATAR_TIME_OUT);
        uploadAvatarFuture = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        uploadAvatarFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                    LogUtil.i(TAG, "upload avatar success, url =" + url);

                    UserUpdateHelper.update(UserInfoFieldEnum.AVATAR, url, new RequestCallbackWrapper<Void>() {
                        @Override
                        public void onResult(int code, Void result, Throwable exception) {
                            if (code == ResponseCode.RES_SUCCESS) {
                                Toast.makeText(ActUserInfo.this, R.string.head_update_success, Toast.LENGTH_SHORT).show();
                                onUpdateDone();
                            } else {
                                Toast.makeText(ActUserInfo.this, R.string.head_update_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); // 更新资料
                } else {
                    Toast.makeText(ActUserInfo.this, R.string.user_info_update_failed, Toast
                            .LENGTH_SHORT).show();
                    onUpdateDone();
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.user_info_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadAvatarFuture = null;
        DialogMaker.dismissProgressDialog();
        updateUI();
    }
}
