package io.github.loop_x.yummywakeup.infrastructure.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chuandong on 15/8/23.
 */
public abstract class BaseFragment extends Fragment implements InitFragment {

    // Root view of fragment
    private View viewContainer;

    // ------------------------------------------------------------------------
    // Fragment Lifecycle
    // ------------------------------------------------------------------------

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("fragment", "onAttach>>" + this.getClass().getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fragment", "onCreate>>" + this.getClass().getName());

        initConfig(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("fragment", "onCreateView>>" + this.getClass().getName());

        viewContainer = inflater.inflate(getLayoutId(), container, false);
        initView(viewContainer);
        initListener();

        return viewContainer;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("fragment", "onViewCreated>>" + this.getClass().getName());

        refresh();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("fragment", "onActivityCreated>>" + this.getClass().getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("fragment", "onStart>>" + this.getClass().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("fragment", "onResume>>" + this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("fragment", "onPause>>" + this.getClass().getName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("fragment", "onSaveInstanceState>>" + this.getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("fragment", "onStop>>" + this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("fragment", "onDestroyView>>" + this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("fragment", "onDestroy>>" + this.getClass().getName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("fragment", "onDetach>>" + this.getClass().getName());
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