package com.stockroompro.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.artjoker.core.views.ArtJokerTextView;
import com.stockroompro.R;

import java.util.Calendar;


public class NavigationDrawer extends RelativeLayout implements View.OnClickListener {
    private DrawerCallback drawerCallback;
    private ArtJokerTextView unreadMessagesCount;
    private ArtJokerTextView unreadNotificationsCount;
    private ArtJokerTextView tvCopyRights;


    public NavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_drawer, this, true);
        unreadMessagesCount = (ArtJokerTextView) view.findViewById(R.id.tv_number_unread_messages);
        unreadNotificationsCount = (ArtJokerTextView) view.findViewById(R.id.tv_number_unread_notifications);
        tvCopyRights = (ArtJokerTextView) findViewById(R.id.tv_copy_rights);
        tvCopyRights.setText(getResources().getString(R.string.menu_bottom_text, Calendar.getInstance().get(Calendar.YEAR)));
    }

    @Override
    public void onClick(View v) {

        if (drawerCallback != null) {
            drawerCallback.onMenuItemClick(v.getId());
        }
    }

    public final void setDrawerCallback(final DrawerCallback drawerCallback) {
        this.drawerCallback = drawerCallback;
    }

    {
        initHolder();
    }

    private void initHolder() {
        setSaveEnabled(true);
        initViews();
        initListeners();

    }

    private void initListeners() {
        findViewById(R.id.button_main_page).setOnClickListener(this);
        findViewById(R.id.button_menu_login).setOnClickListener(this);
        findViewById(R.id.button_personal_account).setOnClickListener(this);
        findViewById(R.id.button_menu_messages).setOnClickListener(this);
        findViewById(R.id.button_menu_favourites).setOnClickListener(this);
        findViewById(R.id.button_menu_exit).setOnClickListener(this);
        findViewById(R.id.button_menu_add_advert).setOnClickListener(this);
        findViewById(R.id.button_my_adverts).setOnClickListener(this);
        findViewById(R.id.button_menu_notifications).setOnClickListener(this);
        tvCopyRights.setOnClickListener(this);
    }

    public interface DrawerCallback {
        void onMenuItemClick(int id);
    }

    public void hideTabsByAuth(boolean isAuth) {
        findViewById(R.id.button_menu_login).setVisibility(isAuth ? View.GONE : View.VISIBLE);
        findViewById(R.id.button_menu_add_advert).setVisibility(isAuth ? View.VISIBLE : View.GONE);
        ((View) findViewById(R.id.button_menu_notifications).getParent()).setVisibility(isAuth ? View.VISIBLE : View.GONE);
        findViewById(R.id.button_personal_account).setVisibility(isAuth ? View.VISIBLE : View.GONE);
        ((View) findViewById(R.id.button_menu_messages).getParent()).setVisibility(isAuth ? View.VISIBLE : View.GONE);
        findViewById(R.id.button_menu_favourites).setVisibility(isAuth ? View.VISIBLE : View.GONE);
        findViewById(R.id.button_menu_exit).setVisibility(isAuth ? View.VISIBLE : View.GONE);
        findViewById(R.id.button_my_adverts).setVisibility(isAuth ? View.VISIBLE : View.GONE);
    }

    public void setUnreadCount(long messagesUnreadCount, long notificationsUnreadCount) {

        if (messagesUnreadCount == 0)
            unreadMessagesCount.setVisibility(GONE);
        else {
            unreadMessagesCount.setText(String.valueOf(messagesUnreadCount));
            unreadMessagesCount.setVisibility(VISIBLE);
        }
        if (notificationsUnreadCount == 0)
            unreadNotificationsCount.setVisibility(GONE);
        else {
            unreadNotificationsCount.setText(String.valueOf(notificationsUnreadCount));
            unreadNotificationsCount.setVisibility(VISIBLE);
        }
    }
}
