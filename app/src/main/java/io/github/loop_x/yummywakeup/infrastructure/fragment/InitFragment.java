package io.github.loop_x.yummywakeup.infrastructure.fragment;

import android.os.Bundle;
import android.view.View;

/**
 * Created by chuandong on 15/8/23.
 */
public interface InitFragment {
    /***
     * Initializes views of fragment
     */
    public void initView(View container);

    /***
     * Initializes configuration (args in bundle) of fragment
     */
    public void initConfig(Bundle bundle);

    /**
     * Get layout Id of fragment
     * @return layout id
     */
    public int getLayoutId();

    /***
     * Initializes listeners of fragment
     */
    public void initListener();
}
