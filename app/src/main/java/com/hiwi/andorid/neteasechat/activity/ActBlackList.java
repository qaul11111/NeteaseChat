package com.hiwi.andorid.neteasechat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hiwi.andorid.neteasechat.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.BlackListChangedNotify;

import java.util.ArrayList;
import java.util.List;

public class ActBlackList extends AppCompatActivity {

    private List<String> blackList = new ArrayList<>();
    private ListView listBlackView;
    private ArrayAdapter<String> arrayAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ActBlackList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_black_list);

        // 初始化黑名单集合
        initBlackList();
        // 初始化黑名单变化监听
        initBlackListObserver();

        listBlackView = findViewById(R.id.list_black);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, blackList);
        listBlackView.setAdapter(arrayAdapter);
    }

    private void initBlackList() {
        blackList = NIMClient.getService(FriendService.class).getBlackList();
    }

    /**
     * 黑名单消息监听
     */
    private void initBlackListObserver() {
        NIMClient.getService(FriendServiceObserve.class)
                .observeBlackListChangedNotify(new Observer<BlackListChangedNotify>() {
                    @Override
                    public void onEvent(BlackListChangedNotify blackListChangedNotify) {
                        // 拉黑的名单集合
                        List<String> addAccountList = blackListChangedNotify.getAddedAccounts();
                        // 移除黑名单的集合
                        List<String> removeAccountList = blackListChangedNotify.getRemovedAccounts();

                        blackList.addAll(addAccountList);
                        blackList.removeAll(removeAccountList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }, true);
    }
}
