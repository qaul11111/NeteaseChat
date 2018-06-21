package com.hiwi.andorid.neteasechat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hiwi.andorid.neteasechat.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

public class ActUserInfoList extends AppCompatActivity {

    private List<String> userAccountList = new ArrayList<>();
    private ListView userInfoListView;
    private ArrayAdapter<String> arrayAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ActUserInfoList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_user_info_list);

        initUserList();
        findView();
        initListener();
    }

    private void initListener() {
        userInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String account = (String) parent.getItemAtPosition(position);
                ActUserInfo.start(ActUserInfoList.this, account);
            }
        });
    }

    private void findView() {
        userInfoListView = findViewById(R.id.list_user_info);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userAccountList);
        userInfoListView.setAdapter(arrayAdapter);
    }

    private void initUserList() {
        List<NimUserInfo> users =  NIMClient.getService(UserService.class).getAllUserInfo();
        for (NimUserInfo userInfo : users) {
            userAccountList.add(userInfo.getAccount());
        }
    }
}
