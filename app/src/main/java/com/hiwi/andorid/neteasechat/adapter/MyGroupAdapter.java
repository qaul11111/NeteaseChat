package com.hiwi.andorid.neteasechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiwi.andorid.neteasechat.R;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

import static com.netease.nrtc.b.b.i;

public class MyGroupAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<Team> data;


    public MyGroupAdapter(Context context) {
        this.inflater=LayoutInflater.from(context);
    }

    public void setData(List<Team> recents){
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

        Team recents = data.get(position);

        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_my_group,null);
            holder.name = convertView.findViewById(R.id.item_my_group_name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.name.setText(recents.getName());
        String icon = recents.getIcon();

        return convertView;

    }


    private class ViewHolder{
        TextView name;
        ImageView imageView;
    }
}
