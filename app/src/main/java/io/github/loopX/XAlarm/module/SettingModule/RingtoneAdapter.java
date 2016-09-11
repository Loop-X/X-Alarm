package io.github.loopX.XAlarm.module.SettingModule;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.view.YummyTextView;

public class RingtoneAdapter extends ArrayAdapter<Object> {

    private  String[] mRingtoneNames = {"WARM BREEZE", "DISTANT MEMORIES", "MORNING MIST", "SUNRISE"};

    private Context mContext;
    private int mLastSelectPosition = -1;

    public RingtoneAdapter(Context context, int resource) {
        this(context, resource, 0);
    }

    public RingtoneAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mRingtoneNames.length;
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
            holder.imRingtoneSelect = (ImageView) view.findViewById(R.id.iv_ringtone_list_item);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvRingtoneTitle.setText(mRingtoneNames[position]);

        if(mLastSelectPosition != position) {
            holder.tvRingtoneTitle.setTextColor(ContextCompat.getColor(mContext, R.color.loopX_3_40_alpha));
            holder.imRingtoneSelect.setImageDrawable(null);
        } else {
            holder.tvRingtoneTitle.setTextColor(ContextCompat.getColor(mContext, R.color.loopX_3));
            holder.imRingtoneSelect.setImageResource(R.drawable.icon_choose_ringtone);
        }

        return view;
    }

    public String getRingtoneName(int position) {
        return mRingtoneNames[position];
    }

    private static class ViewHolder {
        YummyTextView tvRingtoneTitle;
        ImageView imRingtoneSelect;
    }
    
    public int getSelectedPosition(){
        return mLastSelectPosition;
    }

    public void setSelectedPosition(int position) {
        this.mLastSelectPosition = position;
    }
}