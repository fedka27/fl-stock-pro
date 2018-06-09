package com.artjoker.tool.fragments.collections;


import android.app.FragmentTransaction;

public class AnimationCollection {
    private final int[] collection;

    public AnimationCollection(final int enter, final int exit) {
        this.collection = new int[]{enter, exit};
    }

    public AnimationCollection(final int enter, final int exit, final int popEnter, final int popExit) {
        this.collection = new int[]{enter, exit, popEnter, popExit};
    }

    public void setAnimationForTransaction(final FragmentTransaction transaction) {
        switch (collection.length) {
            case 4:
                transaction.setCustomAnimations(collection[0], collection[1], collection[2], collection[3]);
                break;
            case 2:
                transaction.setCustomAnimations(collection[0], collection[1]);
                break;
        }
    }

}