package io.github.loopX.XAlarm.infrastructure;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements AppComponentInitial {

    // Root view of fragment
    private View viewContainer;

    // ToDo get parent activity

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewContainer = inflater.inflate(getLayoutId(), container, false);
        onViewInitial();
        return viewContainer;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onRefreshData();
    }
    
    protected View findViewById(@IdRes int id){
        if (viewContainer == null) return null;
        return viewContainer.findViewById(id);
    }
   
}