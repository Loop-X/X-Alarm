package io.github.loop_x.yummywakeup.infrastructure.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chuandong on 15/8/23.
 */
public abstract class BaseFragment extends Fragment implements InitFragment {

    // Root view of fragment
    private View viewContainer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initConfig(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewContainer = inflater.inflate(getLayoutId(), container, false);
        initView(viewContainer);
        initListener();

        return viewContainer;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refresh();
    }

  

    // ------------------------------------------------------------------------
    // BaseFragment features
    // ------------------------------------------------------------------------

    /**
     * Refreshes View
     */
    public abstract void refresh();

    /**
     * Gets FragmentView
     *
     * @return root view of fragment
     */
    public View getFragmentView() {
        return this.viewContainer;
    }

}