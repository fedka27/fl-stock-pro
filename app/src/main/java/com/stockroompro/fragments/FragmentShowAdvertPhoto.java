package com.stockroompro.fragments;

import android.os.Bundle;
import android.view.View;

import com.stockroompro.R;
import com.stockroompro.fragments.base.BaseApplicationFragment;
import com.stockroompro.views.AdvertPhotoGroup;

import java.util.ArrayList;

/**
 * Created by Alexandr.Bagach on 31.08.2015.
 */
public class FragmentShowAdvertPhoto extends BaseApplicationFragment {

    private static final String PHOTOS_LIST_KEY = "photos_list_key";
    private static final String PHOTO_POSITION_KEY = "photo_position_key";
    private AdvertPhotoGroup photoGroup;

    public static FragmentShowAdvertPhoto newInstance(ArrayList<String> images, int position) {
        FragmentShowAdvertPhoto fragmentShowAdvertPhoto = new FragmentShowAdvertPhoto();
        Bundle args = new Bundle();
        args.putStringArrayList(PHOTOS_LIST_KEY, images);
        args.putInt(PHOTO_POSITION_KEY, position);
        fragmentShowAdvertPhoto.setArguments(args);
        return fragmentShowAdvertPhoto;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_show_advert_photo;
    }

    @Override
    protected void initViews(View view) {
        photoGroup = (AdvertPhotoGroup) view.findViewById(R.id.fragment_show_advert_photo_switcher);
    }

    @Override
    protected void initContent() {
        photoGroup.setPhotos(getArguments().getStringArrayList(PHOTOS_LIST_KEY), getArguments().getInt(PHOTO_POSITION_KEY));
        photoGroup.setCallback(null);
    }

    @Override
    protected int getHeaderIconsPolicy() {
        return 0;
    }
}
