package io.github.loop_x.yummywakeup.module.SettingModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import io.github.loop_x.yummywakeup.R;

public class CustomAdapter extends ArrayAdapter<Object> {

    String[] mRingtoneNames = {"WARM BREEZE", "FOREST GLADE", "MORNING MIST", "SUNRISE"};

    Context mContext;
    int mResourceId;

    public CustomAdapter(Context context, int resource) {
        this(context, resource, 0);
    }

    public CustomAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        mContext = context;
        mResourceId = resource;
    }

    @Override
    public int getCount() {
        return mRingtoneNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            view = inflater.inflate(R.layout.ringtone_list_item, parent, false);

            holder = new ViewHolder();
            holder.tvRingtoneTitle = (TextView) view.findViewById(R.id.tv_ringtone_list_item);
            holder.cbRingtoneSelect = (CheckBox) view.findViewById(R.id.cb_ringtone_list_item);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvRingtoneTitle.setText(mRingtoneNames[position]);

        return view;
    }

    static class ViewHolder {
        TextView tvRingtoneTitle;
        CheckBox cbRingtoneSelect;
    }
}