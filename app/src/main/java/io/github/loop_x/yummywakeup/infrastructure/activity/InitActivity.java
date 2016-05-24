package io.github.loop_x.yummywakeup.infrastructure.activity;

public interface InitActivity {

    /**
     * Initializes Toolbar
     */
    public abstract void initToolbar();

    /**
     * Initializes Views
     */
    public abstract void initView();

    /**
     * Initializes Listener
     */
    public abstract void initListener();

    /**
     * Initializes Data
     */
    public abstract void initData();

    /**
     * Get layout Id of activity
     * @return layout id
     */
    public abstract int getLayoutId();
}
