package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.adapter.MyGroupAdapter;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的群
 */
public class MyGroupAdvanced extends Activity {

    List<Team> data = new ArrayList<>();
    private ListView listView;
    private MyGroupAdapter adapter;
    private String teamTypeEnum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_group_advanced);
        teamTypeEnum = getIntent().getStringExtra("teamTypeEnum");
        init();
        initGroup();
    }

    private void init() {
        listView = findViewById(R.id.act_my_group_advanced_list);
        adapter = new MyGroupAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String teamId = data.get(position).getId();
                NimUIKit.startTeamSession(NimUIKit.getContext(),teamId);
            }
        });
    }


    private void initGroup() {
        NIMClient.getService(TeamService.class).queryTeamList().setCallback(new RequestCallback<List<Team>>() {
            @Override
            public void onSuccess(List<Team> teams) {

                for (Team dto : teams) {
                    // 0聊天室   1 群聊
                    int type = dto.getType().getValue();
                    if(teamTypeEnum.equals("0") && type ==0){
                        data.add(dto);
                    }else if(teamTypeEnum.equals("1") && type ==1){
                        data.add(dto);
                    }
                    adapter.setData(data);
                }
                // 获取成功，teams为加入的所有群组
                Log.e("TAG", "");
            }

            @Override
            public void onFailed(int i) {
                // 获取失败，具体错误码见i参数
            }

            @Override
            public void onException(Throwable throwable) {
                // 获取异常
            }
        });
    }


}
