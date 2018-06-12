package com.hiwi.andorid.neteasechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiwi.andorid.neteasechat.R;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

import static com.netease.nrtc.b.b.i;

public class AdapterRecentConversation extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<RecentContact> data;

    public void setData(List<RecentContact> recents){
        this.data = recents;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
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

        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_recent_conversation,null);
//            holder.tv= (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
//        holder.tv.setText(data.get(i));
        return convertView;

    }


    private class ViewHolder{
        TextView tv;
    }
}
