package com.hiwi.andorid.neteasechat.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.util.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

public class ActAddFriends extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "ActAddFriends";

    private static final int DIRECT_ADD = 1;
    private static final int REQUEST_ADD = 2;

    private EditText editFriendName;
    private EditText editAddMsg;
    private TextView txtAddMsg;
    private Button btnAddFriend;
    private Button btnAcceptAdd;
    private Button btnRefuseAdd;
    private Button btnDeleteFriend;
    private Button btnAddBlack;
    private Button btnRemoveBlack;
    private Button btnBlackList;
    private CheckBox checkDirectAdd;
    private CheckBox checkRequestAdd;

    private String requestAccount;

    /**
     * 启动 ActAddFriends
     *
     * @param context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, ActAddFriends.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_friends);

        txtAddMsg = findViewById(R.id.txt_addMsg);
        btnAddFriend = findViewById(R.id.btn_addFriend);
        btnAcceptAdd = findViewById(R.id.btn_accept_add);
        btnRefuseAdd = findViewById(R.id.btn_refuse_add);
        btnDeleteFriend = findViewById(R.id.btn_delete_friend);
        btnAddBlack = findViewById(R.id.btn_add_black);
        btnRemoveBlack = findViewById(R.id.btn_remove_black);
        btnBlackList = findViewById(R.id.btn_black_list);
        editFriendName = findViewById(R.id.edit_friendName);
        editAddMsg = findViewById(R.id.edit_addMsg);
        checkDirectAdd = findViewById(R.id.check_direct_add);
        checkRequestAdd = findViewById(R.id.check_request_add);

        btnAddFriend.setOnClickListener(this);
        btnAcceptAdd.setOnClickListener(this);
        btnRefuseAdd.setOnClickListener(this);
        btnDeleteFriend.setOnClickListener(this);
        btnAddBlack.setOnClickListener(this);
        btnRemoveBlack.setOnClickListener(this);
        btnBlackList.setOnClickListener(this);

        checkDirectAdd.setOnCheckedChangeListener(this);
        checkRequestAdd.setOnCheckedChangeListener(this);

        // 初始化添加好友消息监听
        initMessageObserver();
        // 初始化界面
        initView();
    }

    private void initView() {
        txtAddMsg.setVisibility(View.GONE);
        btnAcceptAdd.setVisibility(View.GONE);
        btnRefuseAdd.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addFriend: // 添加好友
                if (checkDirectAdd.isChecked() || checkRequestAdd.isChecked()) {
                    if (checkDirectAdd.isChecked()) {
                        addFriend(DIRECT_ADD);
                    }
                    if (checkRequestAdd.isChecked()) {
                        addFriend(REQUEST_ADD);
                    }
                }
                break;
            case R.id.btn_accept_add: // 同意好友请求
                dealWithAddFriendRequest(true);
                break;
            case R.id.btn_refuse_add: // 拒绝好友请求
                dealWithAddFriendRequest(false);
                break;
            case R.id.btn_delete_friend: // 删除好友
                deleteFriend();
                break;
            case R.id.btn_add_black: // 添加黑名单
                addBlackList();
                break;
            case R.id.btn_remove_black: // 移除黑名单
                removeFromBlackList();
                break;
            case R.id.btn_black_list: // 获取黑名单列表
                getBlackList();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_direct_add:
                checkRequestAdd.setClickable(false);
                break;
            case R.id.check_request_add:
                checkDirectAdd.setClickable(false);
                break;
        }
    }

    /**
     * 处理添加好友请求
     *
     * @param type
     */
    private void dealWithAddFriendRequest(final boolean type) {
        NIMClient.getService(FriendService.class).ackAddFriendRequest(requestAccount, type).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (type) {
                    Toast.makeText(ActAddFriends.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActAddFriends.this, "拒绝成功", Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtAddMsg.setText("");
                        txtAddMsg.setVisibility(View.GONE);
                        btnAcceptAdd.setVisibility(View.GONE);
                        btnRefuseAdd.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onFailed(int i) {
                Toast.makeText(ActAddFriends.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                LogUtil.e(TAG, throwable.getMessage());
            }
        });
    }

    private void initMessageObserver() {

        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(new Observer<SystemMessage>() {
            @Override
            public void onEvent(SystemMessage systemMessage) {
                if (systemMessage.getType() == SystemMessageType.AddFriend) {
                    AddFriendNotify attachData = (AddFriendNotify) systemMessage.getAttachObject();
                    if (attachData != null) {
                        // 针对不同的事件做处理
                        if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_DIRECT) {
                            // 对方直接添加你为好友
                            setMessageArea(DIRECT_ADD, systemMessage.getFromAccount(), "");
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_AGREE_ADD_FRIEND) {
                            // 对方通过了你的好友验证请求
                            Toast.makeText(ActAddFriends.this, "对方通过了你的好友验证请求", Toast.LENGTH_SHORT).show();
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_REJECT_ADD_FRIEND) {
                            // 对方拒绝了你的好友验证请求
                            Toast.makeText(ActAddFriends.this, "对方拒绝了你的好友验证请求", Toast.LENGTH_SHORT).show();
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                            // 对方请求添加好友，一般场景会让用户选择同意或拒绝对方的好友请求。
                            // 通过message.getContent()获取好友验证请求的附言
                            requestAccount = systemMessage.getFromAccount();
                            setMessageArea(REQUEST_ADD, requestAccount, systemMessage.getContent());
                        }
                    }
                }
            }
        }, true);
    }

    private void setMessageArea(final int type, final String name, final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtAddMsg.setText("");
                txtAddMsg.setVisibility(View.VISIBLE);
                switch (type) {
                    case DIRECT_ADD:
                        txtAddMsg.setText(name + "直接添加您为好友");
                        break;
                    case REQUEST_ADD:
                        txtAddMsg.setText(name + "请求添加您为好友, 附加信息为: " + content);
                        btnAcceptAdd.setVisibility(View.VISIBLE);
                        btnRefuseAdd.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    /**
     * 添加好友
     */
    private void addFriend(final int type) {
        String friendName = editFriendName.getText().toString();
        if (!TextUtils.isEmpty(friendName)) {
            // DIRECT_ADD	直接加对方为好友
            // VERIFY_REQUEST	发起好友验证请求
            final VerifyType verifyType;
            if (type == DIRECT_ADD) {
                verifyType = VerifyType.DIRECT_ADD;
            } else {
                verifyType = VerifyType.VERIFY_REQUEST;
            }
            String msg = editAddMsg.getText().toString();
            AddFriendData addFriendData = new AddFriendData(friendName, verifyType, msg);
            NIMClient.getService(FriendService.class).addFriend(addFriendData).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ActAddFriends.this, "发送成功", Toast.LENGTH_SHORT).show();
                    checkRequestAdd.setClickable(true);
                    checkDirectAdd.setClickable(true);
                }

                @Override
                public void onFailed(int i) {
                    if (i == 408) {
                        Toast.makeText(ActAddFriends.this, "该好友已经存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActAddFriends.this, "发送验证失败", Toast.LENGTH_SHORT).show();
                    }
                    LogUtil.e(TAG, "发送添加好友失败, 信息: " + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    LogUtil.w(TAG, "发送添加好友异常, 信息: " + throwable.getMessage());
                }
            });
        }
    }

    /**
     * 删除好友
     */
    private void deleteFriend() {
        String friendName = editFriendName.getText().toString();
        if (!TextUtils.isEmpty(friendName)) {
            NIMClient.getService(FriendService.class).deleteFriend(friendName).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ActAddFriends.this, "删除成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(int i) {
                    Toast.makeText(ActAddFriends.this, "删除失败", Toast.LENGTH_SHORT).show();
                    LogUtil.e(TAG, "错误信息为: " + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    LogUtil.w(TAG, "异常信息为: " + throwable.getMessage());
                }
            });
        }
    }

    /**
     * 添加到黑名单
     */
    private void addBlackList() {
        String friendName = editFriendName.getText().toString();
        if (!TextUtils.isEmpty(friendName)) {
            NIMClient.getService(FriendService.class).addToBlackList(friendName).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ActAddFriends.this, "添加至黑名单成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(int i) {
                    Toast.makeText(ActAddFriends.this, "添加至黑名单失败", Toast.LENGTH_SHORT).show();
                    LogUtil.e(TAG, "添加黑名单失败信息: " + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    LogUtil.w(TAG, "添加黑名单异常: " + throwable.getMessage());
                }
            });
        }
    }

    /**
     * 移除黑名单
     */
    private void removeFromBlackList() {
        String friendName = editFriendName.getText().toString();
        if (!TextUtils.isEmpty(friendName)) {
            NIMClient.getService(FriendService.class).removeFromBlackList(friendName).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ActAddFriends.this, "移除黑名单成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(int i) {
                    Toast.makeText(ActAddFriends.this, "移除黑名单失败", Toast.LENGTH_SHORT).show();
                    LogUtil.e(TAG, "移除黑名单失败信息: " + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    LogUtil.w(TAG, "移除黑名单异常信息: " + throwable.getMessage());
                }
            });
        }
    }

    /**
     * 获取黑名单列表
     */
    private void getBlackList() {

    }
}
