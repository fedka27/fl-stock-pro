package com.artjoker.tool.fragments.collections;

import android.util.SparseArray;

public final class AnimationCollectionFactory {
    private static final SparseArray<AnimationCollection> CACHE = new SparseArray<AnimationCollection>();

    private AnimationCollectionFactory() {

    }

    public static AnimationCollectionFactory getInstance() {
        return AnimationCollectionFactoryHolder.INSTANCE;
    }

    public final AnimationCollection get(final int type) {
        AnimationCollection animationCollection = CACHE.get(type);
        if (animationCollection == null) {
            animationCollection = getAnimationCollection(type);
            if (animationCollection != null) {
                CACHE.put(type, animationCollection);
            }
        }
        return null;
    }

    private AnimationCollection getAnimationCollection(int type) {
        switch (type) {
            case Type.ALPHA:
                return new AlphaAnimationCollection();
            case Type.SLIDE:
                return new SlideAnimationCollection();
            default:
                return null;

        }
    }

    public static interface Type {
        int ALPHA = 0x3001;
        int SLIDE = 0x3002;
    }

    public static class AnimationCollectionFactoryHolder {
        public static final AnimationCollectionFactory INSTANCE = new AnimationCollectionFactory();
    }

}