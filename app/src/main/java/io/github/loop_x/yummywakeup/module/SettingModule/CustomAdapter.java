package io.github.loop_x.yummywakeup.module.SettingModule;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import io.github.loop_x.yummywakeup.R;

public class CustomAdapter extends BaseAdapter {

    String[] mStringList;
    Context mContext;
    LayoutInflater mInflater;

    public CustomAdapter(Context context, String[] names) {
        mContext = context;
        mStringList = names;
        mInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return mStringList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.ringtone_list_item, null);

        final CheckedTextView ctvItem = (CheckedTextView) view.findViewById(R.id.ctv_ringtone_list_item);
        ctvItem.setText(mStringList[position]);
        ctvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctvItem.isChecked()) {
                    ctvItem.setCheckMarkDrawable(null);
                    ctvItem.setTextColor(ContextCompat.getColor(mContext, R.color.loopX_3_40_alpha));
                    ctvItem.setChecked(false);
                } else {
                    ctvItem.setCheckMarkDrawable(R.drawable.icon_choose_ringtone);
                    ctvItem.setTextColor(ContextCompat.getColor(mContext, R.color.loopX_3));
                    ctvItem.setChecked(true);
                }
            }
        });

        return view;
    }
}