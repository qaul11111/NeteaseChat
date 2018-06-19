package com.hiwi.andorid.neteasechat.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hiwi.andorid.neteasechat.R;

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

    }

    /**
     * 从服务器搜索群组
     */
    private void searchGroup() {

    }
}
