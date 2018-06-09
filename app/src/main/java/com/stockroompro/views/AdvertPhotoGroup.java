package com.stockroompro.views;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stockroompro.R;

import java.util.ArrayList;

public class AdvertPhotoGroup extends RelativeLayout implements View.OnClickListener, AdvertPhotoSwitcher.OnChangeImageCallback {
    public static final int START_INDEX = 1;
    private ImageButton buttonLeft;
    private ImageButton buttonRight;
    private TextView currentImageIndexView;
    private TextView countImagesView;
    private AdvertPhotoSwitcher photoSwitcher;
    private View childrenLayout;

    public AdvertPhotoGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHolder();
    }

    public AdvertPhotoGroup(Context context) {
        super(context);
        initHolder();

    }

    private void initHolder() {
        initViews();
        initListeners();
    }

    private void initListeners() {
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        photoSwitcher.setCallback(this);
    }

    public void setPhotos(ArrayList<String> photos, int position) {
        photoSwitcher.setPhotosUrls(photos, position);
        countImagesView.setText(String.valueOf(photoSwitcher.getCountOfImages()));
        photoSwitcher.startShowing(true);
        if (photos.size() < 2) {
            buttonLeft.setVisibility(GONE);
            buttonRight.setVisibility(GONE);
            childrenLayout.findViewById(R.id.container_tv_of_count_images).setVisibility(INVISIBLE);
            photoSwitcher.setScrollable(false);
        }
    }

    private void initViews() {
        childrenLayout = LayoutInflater.from(getContext()).inflate(R.layout.children_for_image_switcher, this, true);
        buttonLeft = (ImageButton) childrenLayout.findViewById(R.id.ib_left);
        buttonRight = (ImageButton) childrenLayout.findViewById(R.id.ib_right);
        currentImageIndexView = (TextView) childrenLayout.findViewById(R.id.tv_current_index);
        countImagesView = (TextView) childrenLayout.findViewById(R.id.tv_count_images);
        photoSwitcher = (AdvertPhotoSwitcher) childrenLayout.findViewById(R.id.image_switcher);
        buttonLeft.bringToFront();
        buttonRight.bringToFront();
        childrenLayout.findViewById(R.id.container_tv_of_count_images).bringToFront();
        currentImageIndexView.setText(Integer.toString(START_INDEX));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_left:
                photoSwitcher.previousImage();
                break;
            case R.id.ib_right:
                photoSwitcher.nextImage();
                break;

        }
    }

    @Override
    public void onChangeImage(int position) {
        currentImageIndexView.setText(Integer.toString(position + START_INDEX));
    }

    public void setCallback (AdvertPhotoSwitcher.OnImageClick imageClick) {
        photoSwitcher.setOnImageClick(imageClick);
    }
}
