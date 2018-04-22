package com.jacob.www.a2dolist.mvp.model.data;

import com.yalantis.beamazingtoday.interfaces.BatModel;

/**
 * Created by ASUS-NB on 2017/8/24.
 */

public class Goal implements BatModel {
    private String name;
    private boolean isChecked;
    public Goal(String name){
        this.name = name;
    }
    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public void setChecked(boolean b) {
        isChecked = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
