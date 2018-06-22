package com.hiwi.andorid.neteasechat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.util.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;

import java.util.ArrayList;
import java.util.List;

public class ActNotification extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActNotification";

    private Button btnNoticeClose;
    private Button btnNoticeOpen;
    private Button btnNoticeViewMute;
    private Button btnNoticeCloseMute;
    private EditText txtNoticeAccount;
    private ListView listNoticeMuteView;

    private List<String> muteAccountList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_notification);

        btnNoticeClose = findViewById(R.id.btn_notice_close);
        btnNoticeOpen = findViewById(R.id.btn_notice_open);
        btnNoticeViewMute = findViewById(R.id.btn_notice_view_mute);
        btnNoticeCloseMute = findViewById(R.id.btn_notice_close_mute);
        txtNoticeAccount = findViewById(R.id.txt_notice_account);
        listNoticeMuteView = findViewById(R.id.list_notice_mute);

        btnNoticeClose.setOnClickListener(this);
        btnNoticeOpen.setOnClickListener(this);
        btnNoticeCloseMute.setOnClickListener(this);
        btnNoticeViewMute.setOnClickListener(this);

        initObserveMuteListChangedNotify();
    }

    /**
     * 初始化用户静音列表监听
     */
    private void initObserveMuteListChangedNotify() {
        NIMClient.getService(FriendServiceObserve.class).observeMuteListChangedNotify(
                new Observer<MuteListChangedNotify>() {
                    @Override
                    public void onEvent(MuteListChangedNotify notify) {
                        String account = notify.getAccount();
                        if (notify.isMute()) { // 判断改用户是否被静音
                            muteAccountList.add(account);
                        } else {
                            muteAccountList.remove(account);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        muteAccountList.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notice_open: // 打开消息提醒
                setNotification(true);
                break;
            case R.id.btn_notice_close: // 关闭消息提醒
                setNotification(false);
                break;
            case R.id.btn_notice_view_mute: // 显示静音账号列表
                viewMuteList();
                break;
            case R.id.btn_notice_close_mute:
                closeMuteList(); // 关闭静音列表
                break;
            default:
                break;
        }
    }

    /**
     * 关闭静音列表
     */
    private void closeMuteList() {
        listNoticeMuteView.setVisibility(View.GONE);
    }

    /**
     * 显示静音账号列表
     */
    private void viewMuteList() {
        listNoticeMuteView.setVisibility(View.VISIBLE);
        if (muteAccountList.size() == 0) {
            muteAccountList = NIMClient.getService(FriendService.class).getMuteList();
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, muteAccountList);
        }
        listNoticeMuteView.setAdapter(arrayAdapter);
    }

    /**
     * 设置对应账号的消息提醒
     */
    private void setNotification(final boolean notice) {
        String account = txtNoticeAccount.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断账号现在的消息提醒状态
        boolean notice1 = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        if (notice && notice1) {
            Toast.makeText(this, "该账号已开启提醒", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!notice && !notice1) {
            Toast.makeText(this, "该账号已静音", Toast.LENGTH_SHORT).show();
            return;
        }
        NIMClient.getService(FriendService.class).setMessageNotify(account, notice).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (notice) {
                    Toast.makeText(ActNotification.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActNotification.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailed(int i) {
                if (notice) {
                    Toast.makeText(ActNotification.this, "开启消息提醒失败", Toast.LENGTH_SHORT).show();
                    LogUtil.e(TAG, "开启消息提醒失败信息: " + i);
                } else {
                    Toast.makeText(ActNotification.this, "取消消息提醒失败", Toast.LENGTH_SHORT).show();
                    LogUtil.e(TAG, "取消消息提醒失败信息: " + i);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                if (notice) {
                    LogUtil.w(TAG, "开启消息提醒失败异常: " + throwable.getMessage());
                } else {
                    LogUtil.w(TAG, "取消消息提醒失败异常: " + throwable.getMessage());
                }
            }
        });

    }
}
