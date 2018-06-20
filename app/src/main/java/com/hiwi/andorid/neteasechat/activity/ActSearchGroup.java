package com.hiwi.andorid.neteasechat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.util.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

public class ActSearchGroup extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActSearchGroup";

    private EditText editSearchGroupId;
    private Button btnSearchGroup;
    private Button btnJoinGroup;
    private TextView txtGroupName;

    private String groupId;

    public static void start(Context context) {
        Intent intent = new Intent(context, ActSearchGroup.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_search_group);

        editSearchGroupId = findViewById(R.id.edit_search_group_id);
        btnSearchGroup = findViewById(R.id.btn_search_group);
        btnJoinGroup = findViewById(R.id.btn_search_join);
        txtGroupName = findViewById(R.id.txt_search_group_name);

        btnJoinGroup.setOnClickListener(this);
        btnSearchGroup.setOnClickListener(this);

        initView();
    }

    private void initView() {
        btnJoinGroup.setVisibility(View.INVISIBLE);
        txtGroupName.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_group: // 搜索群组
                searchGroup();
                break;
            case R.id.btn_search_join: // 加入群组
                joinGroup();
                break;
            default:
                break;
        }
    }

    /**
     * 加入群组
     */
    private void joinGroup() {
        NIMClient.getService(TeamService.class).applyJoinTeam(groupId, "").setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                Toast.makeText(ActSearchGroup.this, "申请已发送", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int i) {
                if (i == 809) {
                    Toast.makeText(ActSearchGroup.this, "已经在该群内", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActSearchGroup.this, "申请加入群失败", Toast.LENGTH_SHORT).show();
                }
                LogUtil.e(TAG, "申请加入群失败信息: " + i);

            }

            @Override
            public void onException(Throwable throwable) {
                LogUtil.w(TAG, "申请加入群异常: " + throwable.getMessage());
            }
        });
    }

    /**
     * 从服务器搜索群组
     */
    private void searchGroup() {
        groupId = editSearchGroupId.getText().toString();
        if (TextUtils.isEmpty(groupId)) {
            Toast.makeText(this, "群组 id 不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        NIMClient.getService(TeamService.class).searchTeam(groupId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(final Team team) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtGroupName.setVisibility(View.VISIBLE);
                        btnJoinGroup.setVisibility(View.VISIBLE);
                        txtGroupName.setText(team.getName());
                    }
                });
            }

            @Override
            public void onFailed(int i) {
                switch (i) {
                    case 408:
                        Toast.makeText(ActSearchGroup.this, "客户端请求超时, 请检查是否登录", Toast.LENGTH_SHORT).show();
                        break;
                    case 803:
                        Toast.makeText(ActSearchGroup.this, "不存在该群组", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(ActSearchGroup.this, "搜索群失败", Toast.LENGTH_SHORT).show();
                        break;
                }
                LogUtil.e(TAG, "查询群组出错信息: " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                LogUtil.w(TAG, "查询群组异常信息: " + throwable.getMessage());
            }
        });
    }
}
