package com.hiwi.andorid.neteasechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.adapter.SortAdapter;
import com.hiwi.andorid.neteasechat.custom.side_bar.CharacterParser;
import com.hiwi.andorid.neteasechat.custom.side_bar.ISideBar;
import com.hiwi.andorid.neteasechat.custom.side_bar.PinyinComparator;
import com.hiwi.andorid.neteasechat.custom.side_bar.SideBar;
import com.hiwi.andorid.neteasechat.custom.side_bar.SortMode;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通讯录
 */
public class ActMailList extends AppCompatActivity implements ISideBar, View.OnClickListener {

    private TextView showTv;
    private SideBar bar;
    private SortAdapter adapter;
    private List<SortMode> data = new ArrayList<>();
    private ListView list;
    private CharacterParser characterParser;
    private LayoutInflater inflater;
    private View listHead;
    private Button broupN;
    private Button broupA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mail_ist);
        initView();
        initData();
        initListener();
    }



    private void initView() {
        inflater = LayoutInflater.from(this);

        listHead = inflater.inflate(R.layout.act_mail_list_head,null);
        broupN = listHead.findViewById(R.id.act_mail_list_group_n);
        broupA = listHead.findViewById(R.id.act_mail_list_group_a);

        bar = (SideBar) findViewById(R.id.slidBar);
        showTv = (TextView) findViewById(R.id.showTv);
        list = (ListView) findViewById(R.id.list);
    }

    private void initData() {
        //汉子转拼音的工具类
        characterParser = CharacterParser.getInstance();
        //获取所有好友Id
        List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();

        //根据Id查询好友信息
        List<UserInfo> userInfo = NimUIKit.getUserInfoProvider().getUserInfo(friends);

        for(UserInfo dto : userInfo){
            SortMode mailPeopleBean = new SortMode();
            mailPeopleBean.setAvarat(dto.getAvatar());
            mailPeopleBean.setName(dto.getName());
            String AZ = characterParser.getSelling(dto.getName());
            AZ = AZ.substring(0,1);
            AZ = convertString(AZ,true);
            mailPeopleBean.setSortLetter(AZ);
            data.add(mailPeopleBean);
        }
        //排序
        Collections.sort(data, new PinyinComparator());
        adapter = new SortAdapter(this,data);
        list.addHeaderView(listHead);
        list.setAdapter(adapter);
    }

    private void initListener() {
        bar.setListener(this);
        broupN.setOnClickListener(this);
        broupA.setOnClickListener(this);
    }

    @Override
    public void onTouchLetterChanged(String s) {
        showTv.setText(s);
        showTv.setVisibility(View.VISIBLE);

        int position = adapter.getPositionForSelection(s.charAt(0));
        if (position != -1) {
            list.setSelection(position);
        }
    }

    @Override
    public void onTouchUpInVisibility() {
        showTv.setText("");
        showTv.setVisibility(View.INVISIBLE);
    }


    /**
     * 首字母大小写转换
     * @param str
     * @param beginUp
     * @return
     */
    private String convertString(String str, Boolean beginUp){

        char[] ch = str.toCharArray();
        StringBuffer sbf = new StringBuffer();
        for(int i=0; i< ch.length; i++){
            if(i == 0 && beginUp){//如果首字母需大写
                sbf.append(charToUpperCase(ch[i]));
            }else{
                sbf.append(charToLowerCase(ch[i]));
            }
        }
        return sbf.toString();
    }

    /**转大写**/
    private char charToUpperCase(char ch){
        if(ch <= 122 && ch >= 97){
            ch -= 32;
        }
        return ch;
    }

    /***转小写**/
    private char charToLowerCase(char ch){
        if(ch <= 90 && ch >= 65){
            ch += 32;
        }
        return ch;
    }

    @Override
    public void onClick(View v) {
        if(v == broupN){
            Intent intent = new Intent(this, MyGroupAdvanced.class);
            intent.putExtra("teamTypeEnum", "0");
            startActivity(intent);
        }else if(v == broupA){
            Intent intent = new Intent(this, MyGroupAdvanced.class);
            intent.putExtra("teamTypeEnum", "1");
            startActivity(intent);
        }
    }
}
