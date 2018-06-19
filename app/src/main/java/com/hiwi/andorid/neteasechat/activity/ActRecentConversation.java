package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.adapter.AdapterRecentConversation;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * 最近会话
 */
public class ActRecentConversation extends Activity {


    private ListView list;
    private AdapterRecentConversation adapter;
    private List<RecentContact> data = null;
    private Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ecent_conversation);
        list = findViewById(R.id.recent_conversation_cist);
        button = findViewById(R.id.group_setting);
        adapter = new AdapterRecentConversation(this);
        list.setAdapter(adapter);

        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）

                        data = recents;
                        adapter.setData(data);
                    }
                });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int sessionTypeEnum = data.get(position).getSessionType().getValue();
                switch (sessionTypeEnum){
                    case 0 :
                        NimUIKit.startP2PSession(NimUIKit.getContext(), data.get(position).getContactId());
                    case 1:
                        String ids = data.get(position).getContactId();
                        NimUIKit.startTeamSession(NimUIKit.getContext(),data.get(position).getContactId());
                }

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActRecentConversation.this,GroupSetting.class);
                intent.putExtra("id","498811492");
                startActivity(intent);
            }
        });



        Observer<List<RecentContact>> messageObserver =
                new Observer<List<RecentContact>>() {
                    @Override
                    public void onEvent(List<RecentContact> messages) {

                        System.out.print("");


                    }
                };

    }
}
