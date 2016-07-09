package io.github.loopX.XAlarm.module.SetAlarmModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wx.wheelview.adapter.BaseWheelAdapter;

import io.github.loopX.XAlarm.R;

/**
 * Author UFreedom
 * Date : 2016 七月 01
 */
public class TimePickWheelAdapter extends BaseWheelAdapter<String> {


    private Context mContext;

    public TimePickWheelAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected View bindView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.time_pick_wheel_view_item, parent,false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.timeText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mList.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }


}
