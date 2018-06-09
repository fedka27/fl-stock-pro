package com.darsh.multipleimageselect.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.darsh.multipleimageselect.R;
import com.darsh.multipleimageselect.adapters.CustomImageSelectAdapter;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

/**
 * Created by Darshan on 4/18/2015.
 */
public class ImageSelectActivity extends AppCompatActivity {
    private static final String ACTION_CLOSE = "close_activity";
    private CustomImageSelectAdapter customImageSelectAdapter;
    private GridView gridView;
    private ActionMode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.select_images));
        setContentView(R.layout.activity_image_select);

        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        Intent intent = getIntent();
        ArrayList<Image> images = intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

        customImageSelectAdapter = new CustomImageSelectAdapter(getApplicationContext(), images);

        gridView = (GridView) findViewById(R.id.grid_view_image_select);
        gridView.setAdapter(customImageSelectAdapter);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        gridView.setMultiChoiceModeListener(multiChoiceModeListener);
        gridView.setOnItemClickListener(onItemClickListener);
        orientationBasedUI(getResources().getConfiguration().orientation);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }

    private AbsListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (customImageSelectAdapter.countSelected == 0) {
                mode = null;
            }

            if (mode == null) {
                mode = startActionMode(multiChoiceModeListener);
            }

            if (((CustomImageSelectAdapter) gridView.getAdapter()).getItem(position).isSelected) {
                multiChoiceModeListener.onItemCheckedStateChanged(mode, position, id, false);
            } else {
                multiChoiceModeListener.onItemCheckedStateChanged(mode, position, id, true);
            }
        }
    };

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            customImageSelectAdapter.toggleSelection(position, checked);
            mode.setTitle(String.valueOf(customImageSelectAdapter.countSelected) + getString(R.string.count_selected));
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_contextual_action_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.menu_item_add_image) {
                if (customImageSelectAdapter.countSelected > Constants.limit) {
                    Toast.makeText(getApplicationContext(), Constants.toastDisplayLimitExceed, Toast.LENGTH_LONG).show();
                    return false;
                }
                sendIntent(customImageSelectAdapter.getSelectedImages());
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            customImageSelectAdapter.deselectAll();
            customImageSelectAdapter.countSelected = 0;
        }
    };

    private void orientationBasedUI(int orientation) {
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5);
    }

    private void sendIntent(ArrayList<String> selectedImages) {
        Intent intent = new Intent();
        if (selectedImages != null) {
            intent.putStringArrayListExtra(Constants.INTENT_EXTRA_IMAGES, selectedImages);
            if (getParent() == null) {
                setResult(RESULT_OK, intent);
            } else {
                getParent().setResult(RESULT_OK, intent);
            }
            sendBroadcast(new Intent().setAction(ACTION_CLOSE));
            finish();
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
