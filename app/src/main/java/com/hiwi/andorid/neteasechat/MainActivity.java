package com.hiwi.andorid.neteasechat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.activity.ActLogin;
import com.hiwi.andorid.neteasechat.activity.ActRecentConversation;
import com.hiwi.andorid.neteasechat.activity.ActRegister;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
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

        login = (Button) findViewById(R.id.login);
        register =(Button) findViewById(R.id.register);
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
        if(v == login){
            //登录
            System.out.print("");
            Intent intent = new Intent(this, ActLogin.class);
            startActivity(intent);
        }else if(v == register){
            //注册
            Intent intent = new Intent(this, ActRegister.class);
            startActivity(intent);
        }else if(v == chatRecently){
            //最近会话
            Intent intent = new Intent(this, ActRecentConversation.class);
            startActivity(intent);
        }else if(v == chatRecently){
            //设置
            Toast.makeText(MainActivity.this,"3",Toast.LENGTH_SHORT);
        }else if(v == singleChat){
            //单聊
            Context c = NimUIKit.getContext();
            NimUIKit.startP2PSession(NimUIKit.getContext(), "qaul22222");
        }else if(v == discussionGroup){
            //讨论组聊天
        }else if(v == groupChat){
            //群聊
            NimUIKit.startTeamInfo(NimUIKit.getContext(), "qaul22222");
        }else if(v == addFriend){
            //添加好友
        }else if(v == searchGroup){
            //搜索高级群
        }else if(v == createGroup){
            //创建讨论组
        }else if(v == createSeniorGroup){
            //创建高级群
        }else if(v == setting){
            //设置
        }else if(v == ougOut){
            //注销
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


}
