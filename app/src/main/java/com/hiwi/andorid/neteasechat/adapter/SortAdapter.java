package com.hiwi.andorid.neteasechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.custom.side_bar.SortMode;

import java.util.List;

public class SortAdapter extends BaseAdapter{
    private Context context;
    private List<SortMode> modes;
    private LayoutInflater inflater;

    public SortAdapter(Context context,List<SortMode> modes){
        this.context = context;
        this.modes = modes;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return modes.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return modes.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //每个条目长什么样子
        ViewHolder holder = null;
        SortMode sortMode = modes.get(position);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sidebar, null);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            holder.tvName = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //获取首字母的assii 值
        int selection = sortMode.getSortLetter().charAt(0);
        //通过首字母的assii值来判断哪个position要显示字母
        int positionForSelection = getPositionForSelection(selection);
        if(position == positionForSelection){
            //说明该条目符合显示字母的条件
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(sortMode.getSortLetter());
        }else{
            holder.tvLetter.setVisibility(View.GONE);
        }

        holder.tvName.setText(sortMode.getName());//设置名字
        return convertView;
    }


    public int getPositionForSelection(int selection) {
        for(int i = 0;i<getCount();i++){
            String sortName = modes.get(i).getSortLetter();
            char firstLetter = sortName.toUpperCase().charAt(0);
            if(firstLetter == selection){
                return i;
            }
        }
        return -1;
    }

    class ViewHolder{
        TextView tvLetter;
        TextView tvName;
    }
}

