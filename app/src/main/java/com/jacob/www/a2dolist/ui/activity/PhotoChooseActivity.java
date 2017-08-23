package com.jacob.www.a2dolist.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.jacob.www.a2dolist.R;
import com.jacob.www.a2dolist.mvp.model.data.EventEntry;
import com.jacob.www.a2dolist.util.RxBus;
import com.litao.android.lib.BaseGalleryActivity;
import com.litao.android.lib.Configuration;
import com.litao.android.lib.entity.PhotoEntry;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS-NB on 2017/8/23.
 */

public class PhotoChooseActivity extends BaseGalleryActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.inject(this);
        setTitle("Photo");
        setTitleColor(R.color.white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        attachFragment(R.id.gallery_root);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Configuration getConfiguration() {
        Configuration cfg = new Configuration.Builder()
                .hasCamera(true)
                .hasShade(true)
                .hasPreview(true)
                .setSpaceSize(3)
                .setPhotoMaxWidth(120)
                .setCheckBoxColor(0xFF3F51B5)
                .setDialogHeight(Configuration.DIALOG_HALF)
                .setDialogMode(Configuration.DIALOG_GRID)
                .setMaximum(1)
                .setTip(null)
                .setAblumsTitle(null)
                .build();
        return cfg;
    }

    @Override
    public List<PhotoEntry> getSelectPhotos() {
        return null;
    }

    @Override
    public void onSelectedCountChanged(int i) {

    }

    @Override
    public void onAlbumChanged(String s) {
        getSupportActionBar().setSubtitle(s);
    }

    @Override
    public void onTakePhoto(PhotoEntry photoEntry) {
        RxBus.getInstance().post(photoEntry);
        finish();
    }

    @Override
    public void onChoosePhotos(List<PhotoEntry> list) {
        RxBus.getInstance().post(new EventEntry(list));
        finish();
    }

    @Override
    public void onPhotoClick(PhotoEntry photoEntry) {

    }

    @OnClick({R.id.album, R.id.send_photos})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.album:
                openAlbum();
                break;
            case R.id.send_photos:
                sendPhotos();
                break;
        }
    }
}
