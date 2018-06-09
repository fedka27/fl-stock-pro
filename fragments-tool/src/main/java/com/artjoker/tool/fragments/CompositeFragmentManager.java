package com.artjoker.tool.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.artjoker.tool.fragments.collections.AnimationCollection;

import java.util.Set;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public final class CompositeFragmentManager implements FragmentManager.OnBackStackChangedListener {
    private final FragmentManager manager;
    private final int holderId;
    private Callback callback;

    public CompositeFragmentManager(final FragmentManager manager, final int holderId) {
        this.manager = manager;
        this.holderId = holderId;
        manager.addOnBackStackChangedListener(this);
    }

    private boolean equalBundles(final Bundle currentBundle, final Bundle newBundle) {
        if (currentBundle == null && newBundle == null) {
            return true;
        } else if (currentBundle != null && newBundle != null) {
            Set<String> currentSet = currentBundle.keySet();
            Set<String> newSet = newBundle.keySet();
            if (!currentSet.containsAll(newSet)) {
                return false;
            }

            Object currentValue;
            Object newValue;
            for (String key : currentSet) {
                currentValue = currentBundle.get(key);
                newValue = newBundle.get(key);
                if (currentValue instanceof Bundle && newValue instanceof Bundle && !equalBundles((Bundle) currentValue, (Bundle) newValue)) {
                    return false;
                } else if (currentValue == null) {
                    if (newValue != null || !newBundle.containsKey(key)) {
                        return false;
                    }
                } else if (!currentValue.equals(newValue)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public final void initCommit(final Fragment fragment, final String backStackTag) {
        manager.beginTransaction().add(holderId, fragment, backStackTag).commit();
    }

    public final void remove(final Fragment fragment) {
        manager.beginTransaction().remove(fragment).commit();
    }

    public final void setPrimary(final Fragment fragment) {
        manager.beginTransaction().remove(manager.findFragmentById(holderId)).add(holderId, fragment).commit();
    }

    public final void setPrimary(final Fragment fragment, final String backStackTag) {
        manager.beginTransaction().remove(manager.findFragmentById(holderId)).addToBackStack(backStackTag).add(holderId, fragment, backStackTag).commit();
    }

    public final void setPrimaryAfterPopBack(final Fragment fragment) {
        manager.popBackStackImmediate(null, POP_BACK_STACK_INCLUSIVE);
        setPrimary(fragment, null);
    }

    public final void commit(final Fragment fragment, final String backStackTag, final AnimationCollection collection) {
        final FragmentTransaction transaction = manager.beginTransaction();
        if (collection != null) {
            collection.setAnimationForTransaction(transaction);
        }
        transaction.replace(holderId, fragment).addToBackStack(backStackTag).commit();
    }

    public final void commit(final Fragment fragment, final AnimationCollection collection) {
        final FragmentTransaction transaction = manager.beginTransaction();
        if (collection != null) {
            collection.setAnimationForTransaction(transaction);
        }
        transaction.replace(holderId, fragment).commit();
    }

    public final Fragment getCurrent() {
        return manager.findFragmentById(holderId);
    }

    public final boolean matchClass(final Fragment fragment, final Class cls) {
        return cls.equals(fragment.getClass());
    }

    public final boolean currentMatchClass(final Class cls) {
        return matchClass(getCurrent(), cls);
    }

    public final boolean currentMatchArguments(final Bundle args) {
        return equalBundles(getCurrent().getArguments(), args);
    }

    public final boolean matchCurrent(final Fragment fragment) {
        return currentMatchClass(fragment.getClass()) && currentMatchArguments(fragment.getArguments());
    }

    public final boolean matchCurrent(final Fragment fragment, final Bundle args) {
        return currentMatchClass(fragment.getClass()) && currentMatchArguments(args);
    }

    public final void immediatePopBack(final String tag) {
        manager.popBackStackImmediate(tag, POP_BACK_STACK_INCLUSIVE);
    }

    public final void immediatePopBack() {
        manager.popBackStackImmediate();
    }

    public final void popBack(final String tag) {
        manager.popBackStack(tag, POP_BACK_STACK_INCLUSIVE);
    }

    public final void popBack() {
        manager.popBackStack();
    }

    public final boolean isArgumentsContainsKey(final Fragment fragment, final String key) {
        return fragment.getArguments() != null && fragment.getArguments().containsKey(key);
    }

    public final void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onBackStackChanged() {
        if (callback != null) {
            callback.onChange(getCurrent());
        }
    }

    public interface Callback {
        void onChange(Fragment fragment);
    }

}