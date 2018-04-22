package com.jacob.www.a2dolist.mvp.model.data;

/**
 * Created by ASUS-NB on 2017/8/25.
 */

public class IsCheckedEntry {

    private boolean isChecked;
    private int pos;

    public IsCheckedEntry(boolean isChecked, int pos) {
        this.isChecked = isChecked;
        this.pos = pos;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
