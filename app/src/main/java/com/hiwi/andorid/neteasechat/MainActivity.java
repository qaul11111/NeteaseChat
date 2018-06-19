package com.hiwi.andorid.neteasechat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.activity.ActAddFriends;
import com.hiwi.andorid.neteasechat.activity.ActLogin;
import com.hiwi.andorid.neteasechat.activity.ActMailList;
import com.hiwi.andorid.neteasechat.activity.ActRecentConversation;
import com.hiwi.andorid.neteasechat.activity.ActRegister;
import com.hiwi.andorid.neteasechat.team.TeamCreateHelper;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;

    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private Button login;
    private Button register;
    private Button chatRecently;
    private Button mailList;
    private Button singleChat;
    private Button discussionGroup;
    private Button groupChat;
    private Button addFriend;
    private Button searchGroup;
    private Button single_chat;
    private Button createGroup;
    private Button createSeniorGroup;
    private Button setting;
    private Button ougOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initListener();
        requestBasicPermission();
    }

    private void initListener() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        chatRecently.setOnClickListener(this);
        mailList.setOnClickListener(this);
        singleChat.setOnClickListener(this);
        discussionGroup.setOnClickListener(this);
        groupChat.setOnClickListener(this);
        addFriend.setOnClickListener(this);
        searchGroup.setOnClickListener(this);
        single_chat.setOnClickListener(this);
        createGroup.setOnClickListener(this);
        createSeniorGroup.setOnClickListener(this);
        setting.setOnClickListener(this);
        ougOut.setOnClickListener(this);
    }



    private void init() {
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        chatRecently = findViewById(R.id.chat_recently);
        mailList = findViewById(R.id.mail_list);
        singleChat = findViewById(R.id.single_chat);
        discussionGroup = findViewById(R.id.discussion_group);
        groupChat = findViewById(R.id.group_chat);
        addFriend = findViewById(R.id.add_friend);
        searchGroup = findViewById(R.id.search_group);
        single_chat = findViewById(R.id.single_chat);
        createGroup = findViewById(R.id.create_group);
        createSeniorGroup = findViewById(R.id.create_senior_group);
        setting = findViewById(R.id.setting);
        ougOut = findViewById(R.id.oug_out);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: // 登录
                System.out.print("");
                Intent intent = new Intent(this, ActLogin.class);
                startActivity(intent);
                break;
            case R.id.register: // 注册
                intent = new Intent(this, ActRegister.class);
                startActivity(intent);
                break;
            case R.id.chat_recently: // 最近会话
                intent = new Intent(this, ActRecentConversation.class);
                startActivity(intent);
                break;
            case R.id.single_chat: // 单聊
                NimUIKit.startP2PSession(NimUIKit.getContext(), "qaul22222");
                break;
            case R.id.discussion_group: // 讨论组
                break;
            case R.id.group_chat: // 群聊
                NimUIKit.startTeamInfo(NimUIKit.getContext(), "qaul22222");
                break;
            case R.id.add_friend: // 添加好友
                ActAddFriends.start(this);
                break;
            case R.id.search_group: // 搜索高级群
                break;
            case R.id.create_group: // 创建讨论组
                ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
                NimUIKit.startContactSelector(MainActivity.this, option, REQUEST_CODE_NORMAL);
                break;
            case R.id.create_senior_group: // 创建高级群
                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
                NimUIKit.startContactSelector(MainActivity.this, advancedOption, REQUEST_CODE_ADVANCED);
                break;
            case R.id.setting: // 设置
                break;
            case R.id.oug_out: // 注销
                break;
            case R.id.mail_list: // 通讯录
                intent = new Intent(this,ActMailList.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        try {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        try {
            Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(MainActivity.this, selected, false, null);
                } else {
                    Toast.makeText(MainActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                TeamCreateHelper.createAdvancedTeam(MainActivity.this, selected);
            }
        }
    }

}
