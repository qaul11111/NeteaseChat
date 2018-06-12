package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.adapter.AdapterRecentConversation;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;


public class ActRecentConversation extends Activity {


    private ListView list;
    private AdapterRecentConversation adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ecent_conversation);
        list = findViewById(R.id.recent_conversation_cist);
        adapter = new AdapterRecentConversation();
        list.setAdapter(adapter);

        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）

                        List<RecentContact> data = recents;
                        adapter.setData(data);


                    }
                });
    }
}
