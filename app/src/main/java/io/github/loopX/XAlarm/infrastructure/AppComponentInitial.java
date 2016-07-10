package io.github.loopX.XAlarm.infrastructure;

public interface AppComponentInitial {

    /**
     * Initializes Views
     */
    public abstract void onViewInitial();
    
    
    /**
     * Initializes Data
     */
    public abstract void onRefreshData();

    /**
     * Get layout Id of activity
     * @return layout id
     */
    public abstract int getLayoutId();
}
