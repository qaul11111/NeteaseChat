package com.hiwi.andorid.neteasechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiwi.andorid.neteasechat.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

import static com.netease.nrtc.b.b.i;
import static com.netease.nrtc.b.b.t;

public class AdapterRecentConversation extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<RecentContact> data;


    public AdapterRecentConversation(Context context) {
        this.inflater=LayoutInflater.from(context);
    }

    public void setData(List<RecentContact> recents){
        this.data = recents;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null == data)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RecentContact recents = data.get(position);
        // (-1, "Unknown"),text(0, ""),image(1, "图片"),audio(2, "语音"),video(3, "视频"),location(4, "位置"),file(6, "文件"),
        //avchat(7, "音视频通话"),notification(5, "通知消息"),tip(10, "提醒消息"),robot(11, "机器人消息"),custom(100, "自定义消息");
        int type  = recents.getMsgType().getValue();

        //会话类型  P2P:0   Team：1   System：10001   ChatRoom：10002

        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_recent_conversation,null);
            holder.tv = convertView.findViewById(R.id.item_recent_conversatio_tv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        switch (type){
            case 0 :
                holder.tv.setText("文字信息：" + recents.getContent());break;
            case 1 :
                holder.tv.setText("图片信息：");break;
            case 2 :
                holder.tv.setText("语音信息：");break;
            case 3 :
                holder.tv.setText("视频信息：");break;
            case 4 :
                holder.tv.setText("位置信息：");break;
            case 5 :
                holder.tv.setText("通知信息：");break;
            case 6 :
                holder.tv.setText("文件信息：");break;
            case 7 :
                holder.tv.setText("音视频通话信息：");break;
            case 10 :
                holder.tv.setText("tip提醒消息信息：");break;
            case 11 :
                holder.tv.setText("机器人消息：");break;
            case 100 :
                holder.tv.setText("自定义消息：");break;
        }



        return convertView;

    }


    private class ViewHolder{
        TextView tv;
    }
}
