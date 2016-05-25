package io.github.loop_x.yummywakeup.module.UnlockType.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.UnlockType.Model.UnlockTypeListViewItem;

public class UnlockTypeListViewAdapter extends ArrayAdapter<UnlockTypeListViewItem> {


    private Context mContext;

    public UnlockTypeListViewAdapter(Context context, int resource, List<UnlockTypeListViewItem> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        UnlockTypeListViewItem item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.fragment_unlock_type_item, null);

            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.fg_unlock_type_item_iv);
            holder.tv_title = (TextView) convertView.findViewById(R.id.fg_unlock_type_item_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.iv_icon.setImageResource(item.getImageId());
        holder.tv_title.setText(item.getTitle());

        return convertView;
    }


}
