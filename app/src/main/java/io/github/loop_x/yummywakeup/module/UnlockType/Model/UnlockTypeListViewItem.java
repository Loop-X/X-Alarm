package io.github.loop_x.yummywakeup.module.UnlockType.Model;

public class UnlockTypeListViewItem {

    private int imageId;
    private String title;

    public UnlockTypeListViewItem(int imageId, String title) {
        this.imageId = imageId;
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
