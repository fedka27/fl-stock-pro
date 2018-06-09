package com.stockroompro.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.stockroompro.R;

import java.util.ArrayList;

public class AdvertPhotoSwitcher extends ImageSwitcher implements GestureDetector.OnGestureListener, ViewSwitcher.ViewFactory {

    private static final String IMAGE_MIME_TYPE = "image/*";
    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 80;
    private int position = 0;
    private OnChangeImageCallback onChangeImageCallback;
    private Context context;
    private GestureDetector gestureDetector;
    private Animation slideInLeft;
    private Animation slideInRight;
    private Animation slideOutLeft;
    private Animation slideOutRight;
    private OnImageClick onImageClick;

    private boolean isScrollable = true;

    private ArrayList<String> photosUrls = new ArrayList<>();

    public AdvertPhotoSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AdvertPhotoSwitcher(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setCallback(OnChangeImageCallback callback) {
        onChangeImageCallback = callback;
    }

    private void init() {
        slideInLeft = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        slideOutRight = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        slideInRight = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        slideOutLeft = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
        gestureDetector = new GestureDetector(context, this);
        this.setFactory(this);
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public void setPositionNext() {
        position++;
        if (position > photosUrls.size() - 1) {
            position = 0;
        }
    }

    public void setPositionPrev() {
        position--;
        if (position < 0) {
            position = photosUrls.size() - 1;
        }
    }

    @Override
    public View makeView() {
        ImageView imgview = new ImageView(context);
        imgview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return imgview;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (onImageClick != null) {
            onImageClick.onImageClick(photosUrls, position);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isScrollable) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                        if (diffX > 0) {
                            previousImage();
                        } else {
                            nextImage();
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    public void previousImage() {
        setPositionPrev();
        this.setInAnimation(slideInLeft);
        this.setOutAnimation(slideOutRight);
        ImageLoader.getInstance().loadImage(photosUrls.get(position), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                setImageDrawable(new BitmapDrawable(null, loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        onChangeImageCallback.onChangeImage(position);
    }

    public void nextImage() {
        setPositionNext();
        this.setInAnimation(slideInRight);
        this.setOutAnimation(slideOutLeft);
        ImageLoader.getInstance().loadImage(photosUrls.get(position), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                setImageDrawable(new BitmapDrawable(null, loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        onChangeImageCallback.onChangeImage(position);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gestureDetector.onTouchEvent(ev)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return false;
    }

    public int getCountOfImages() {
        return photosUrls.size();
    }

    public interface OnChangeImageCallback {
        public void onChangeImage(int position);
    }

    public void startShowing(boolean isShowing) {
        if(isShowing && photosUrls.size() > 0) {
            ImageLoader.getInstance().displayImage(photosUrls.get(position), (ImageView) this.getCurrentView());
        }
    }

    public void setPhotosUrls(ArrayList<String> photosUrls, int position) {
        this.photosUrls = photosUrls;
        this.position = position;
    }

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    public interface OnImageClick {
        void onImageClick(ArrayList<String> photos, int position);
    }
}
