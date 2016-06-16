package io.github.loop_x.yummywakeup.module.SettingModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.view.YummyTextView;

public class CustomAdapter extends ArrayAdapter<Object> {

    String[] mRingtoneNames = {"WARM BREEZE", "FOREST GLADE", "MORNING MIST", "SUNRISE"};
    Integer selected_position = -1;

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
            holder.tvRingtoneTitle = (YummyTextView) view.findViewById(R.id.tv_ringtone_list_item);
            holder.cbRingtoneSelect = (CheckBox) view.findViewById(R.id.cb_ringtone_list_item);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvRingtoneTitle.setText(mRingtoneNames[position]);

        //holder.cbRingtoneSelect.setChecked(position == selected_position);
        //holder.cbRingtoneSelect.setOnClickListener(onStateChangedListener(holder.cbRingtoneSelect, position));

        return view;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        YummyTextView tvRingtoneTitle;
        CheckBox cbRingtoneSelect;
    }
}