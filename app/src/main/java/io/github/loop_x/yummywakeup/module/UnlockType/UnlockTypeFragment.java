package io.github.loop_x.yummywakeup.module.UnlockType;


import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.infrastructure.BaseFragment;
import io.github.loop_x.yummywakeup.module.UnlockType.Adapter.UnlockTypeListViewAdapter;
import io.github.loop_x.yummywakeup.module.UnlockType.Model.UnlockTypeListViewItem;

public class UnlockTypeFragment extends BaseFragment {

    private static final String[] titles = new String[] {
            "NORMAL",
            "MATH",
            "PAINT",
            "SHAKE" };

    private static final Integer[] images = {
            R.drawable.model_normal,
            R.drawable.model_math,
            R.drawable.model_paint,
            R.drawable.model_shake };

    private ListView listView;
    private List<UnlockTypeListViewItem> items;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_unlock_type;
    }

    @Override
    public void onViewInitial() {

        // ToDo Change status bar color

        items = new ArrayList<UnlockTypeListViewItem>();

        for (int i = 0; i < titles.length; i++) {
            UnlockTypeListViewItem item = new UnlockTypeListViewItem(images[i], titles[i]);
            items.add(item);
        }

        listView = (ListView) findViewById(R.id.fg_unlock_type_list);

        // ToDo get parent activity from BaseFragment
        UnlockTypeListViewAdapter adapter = new UnlockTypeListViewAdapter(getActivity(),
                R.layout.fragment_unlock_type_item, items);
        listView.setAdapter(adapter);

    }

    @Override
    public void onRefreshData() {

    }


}
