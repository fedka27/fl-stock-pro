package com.stockroompro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.artjoker.core.BackgroundUtils;
import com.artjoker.core.CommonBundleBuilder;
import com.artjoker.core.activities.AbstractLauncher;
import com.artjoker.core.activities.HeaderIconsPolicy;
import com.artjoker.core.fragments.AbstractBasic;
import com.artjoker.core.network.NotificationsPolicy;
import com.artjoker.core.network.RequestDescriptorWithLoadFromDB;
import com.artjoker.tool.core.Notification;
import com.artjoker.tool.core.Preferences;
import com.artjoker.tool.core.SystemHelper;
import com.artjoker.tool.core.TextUtils;
import com.artjoker.tool.core.Ui;
import com.artjoker.tool.fragments.collections.AnimationCollectionFactory;
import com.stockroompro.adapters.ActivityLauncherLocationSpinnerAdapter;
import com.stockroompro.adapters.LocationSpinnerAdapter;
import com.stockroompro.api.models.requests.CityRequest;
import com.stockroompro.api.models.requests.CountryRequest;
import com.stockroompro.api.models.requests.RegionRequest;
import com.stockroompro.database.DatabaseProvider;
import com.stockroompro.fragments.AddAdvertisementFragment;
import com.stockroompro.fragments.AddAdvertisementFragment.Config;
import com.stockroompro.fragments.AdvertListFragment;
import com.stockroompro.fragments.AuthorizationFragment;
import com.stockroompro.fragments.CategoryFragment;
import com.stockroompro.fragments.ChatFragment;
import com.stockroompro.fragments.EditAdvertFragment;
import com.stockroompro.fragments.FavoritesFragment;
import com.stockroompro.fragments.FavouritesMode;
import com.stockroompro.fragments.FragmentAdvertAdded;
import com.stockroompro.fragments.MessagesByUserFragment;
import com.stockroompro.fragments.MyAdvertisementsFragment;
import com.stockroompro.fragments.NotificationFragment;
import com.stockroompro.fragments.ProfileDeletedFragment;
import com.stockroompro.fragments.ProfileFragment;
import com.stockroompro.fragments.SearchResultsFragment;
import com.stockroompro.loaders.LoadersId;
import com.stockroompro.loaders.LocalBroadcastRequestProcessor;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.columns.CategoryColumns;
import com.stockroompro.models.contracts.CityContract;
import com.stockroompro.models.contracts.CountryContract;
import com.stockroompro.models.contracts.MessageByAdvertContract;
import com.stockroompro.models.contracts.RegionContract;
import com.stockroompro.models.spinners.Location;
import com.stockroompro.models.spinners.SpinnerItem;
import com.stockroompro.services.ClearFavouritesService;
import com.stockroompro.services.ClearNotificationsService;
import com.stockroompro.services.GcmIntentService;
import com.stockroompro.services.UpdateMessagesAndNotificationsService;
import com.stockroompro.services.UpdateService;
import com.stockroompro.socialnetworks.FacebookNetwork;
import com.stockroompro.socialnetworks.VKNetwork;
import com.stockroompro.utils.AddUserLocationIntoBundle;
import com.stockroompro.views.LauncherHeaderView;
import com.stockroompro.views.NavigationDrawer;
import com.vk.sdk.VKSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static android.support.v4.view.GravityCompat.END;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.COUNTRY_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.REGION_ID;
import static com.stockroompro.api.models.requests.RequestParams.ParamNames.USER_ID;
import static com.stockroompro.broadcasts.GcmBroadcastReceiver.NotificationHelper;


public class Launcher extends AbstractLauncher implements DrawerLayout.DrawerListener, LauncherHeaderView.HeaderCallback,
        NavigationDrawer.DrawerCallback, LoaderManager.LoaderCallbacks<Cursor>, LocationSpinnerAdapter.OnLocationSpinnerItemClickListener,
        OnClickListener {
    public static final String TAG = "Application";
    public static final String LOCATION_CHANGED = "com.stockroom.location.changed";
    private static final String HIDE_ADD_ADVERT_PROGRESS_BAR = "com.artjoker.core.newtwork.HIDE_PROGRESS";
    public static final String DEFAULT_NAME = "";
    public static final String PREVIOUS_USER_KEY = "PreviousUser";
    public static final String PREVIOUS_ELEMENT = "\u2190";

    private DrawerLayout drawerHolder;
    private NavigationDrawer navigationDrawer;
    private LauncherHeaderView headerView;
    private LinearLayout locationSpinnerContainer;
    private View progressBar;
    private int requestsCount;

    protected int locationType = Config.TYPE_COUNTRY;
    private Spinner locationSpinner;
    private ImageButton buttonConfirm;
    private ActivityLauncherLocationSpinnerAdapter adapter;
    public ArrayList<Location> locationArrayList = new ArrayList<>(3);
    private ArrayList<String> userSearchLocation = new ArrayList<>();
    private boolean userSearchLocationFromPreferences = false;
    protected boolean isLocationItemsLoading = false;

    @Override
    protected void initDependencies() {
        Log.e(TAG, "initDependencies: start");
        SystemHelper.getInstance().setContext(getApplicationContext());
        TextUtils.getInstance().setContext(getApplicationContext());
        AddUserLocationIntoBundle.getInstance().setContext(getApplicationContext());
        startService(new Intent(this, UpdateService.class));
        Log.e(TAG, "initDependencies: end");
    }


    @Override
    protected void initViews() {
        super.initViews();
        Log.e(TAG, "initViews: start");
        drawerHolder = (DrawerLayout) findViewById(R.id.drawer_holder);
        navigationDrawer = (NavigationDrawer) findViewById(R.id.navigation_drawer);
        headerView = (LauncherHeaderView) findViewById(R.id.action_bar);
        progressBar = headerView.findViewById(R.id.progress);
        locationSpinner = (Spinner) findViewById(R.id.action_bar_location_spinner);
        buttonConfirm = (ImageButton) findViewById(R.id.activity_launcher_button_confirm);
        locationSpinnerContainer = (LinearLayout) findViewById(R.id.activity_launcher_location_container);

        final Activity thisActivity = this;
        drawerHolder.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Ui.hideKeyboard(thisActivity, drawerHolder);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        Log.e(TAG, "initViews: end");
    }

    public void setMainTitle(String text) {
        headerView.setMainTitle(text);
    }

    public void setLocationSpinnerContainer(int visibility) {
        locationSpinnerContainer.setVisibility(visibility);
    }

    @Override
    protected void initContent() {
        if (PersonalData.getInstance(this).getUserSearchLocation() != null) {
            userSearchLocation = PersonalData.getInstance(this).getUserSearchLocation();
            for (String item : userSearchLocation) {
                locationArrayList.add(new Location(item));
            }
            userSearchLocationFromPreferences = true;
        }
        getLoaderManager().restartLoader(LoadersId.LAUNCHER_NETWORK_COUNTRY_LOADER_ID, null, this);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        headerView.setHeaderCallback(this);
        drawerHolder.setDrawerListener(this);
        buttonConfirm.setOnClickListener(this);
        navigationDrawer.setDrawerCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAuthorized();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_ACTION);
        filter.addAction(BackgroundUtils.REQUEST_COUNT_CHANGED_ACTION);
        filter.addAction(FragmentAdvertAdded.ADVERT_CREATED);
        filter.addAction(HIDE_ADD_ADVERT_PROGRESS_BAR);
        LocalBroadcastManager.getInstance(this).registerReceiver(onRequestResultReceiver, new IntentFilter(filter));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences(LocalBroadcastRequestProcessor.DIALOG_PREFS, Context.MODE_PRIVATE).edit().clear().commit();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onRequestResultReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, UpdateService.class));
    }

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected int getContentViewContainerId() {
        return R.id.container;
    }

    @Override
    protected Fragment getInitFragment() {
        return new CategoryFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onChange(Fragment fragment) {

    }

    @Override
    public void onEvent(int type, Bundle data) {

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == DrawerLayout.STATE_IDLE) {
            isAuthorized();
        }
    }

    @Override
    public void onMenuClick() {
        Ui.toggleDrawer(drawerHolder);
    }

    private void isAuthorized() {
        if (PersonalData.getInstance(this).getToken() != null) {
            navigationDrawer.hideTabsByAuth(true);
            startService(new Intent(getApplicationContext(), UpdateMessagesAndNotificationsService.class));
            navigationDrawer.setUnreadCount(PersonalData.getInstance(this).getUserUnreadMessagesCount(), PersonalData.getInstance(this).getUserUnreadNotificationsCount());
        } else
            navigationDrawer.hideTabsByAuth(false);
    }

    @Override
    public void onBackClick() {

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            FragmentManager.BackStackEntry backStackEntryAt = getFragmentManager().getBackStackEntryAt(count - 1);
            if (backStackEntryAt != null && ProfileDeletedFragment.class.getCanonicalName().equals(backStackEntryAt.getName())) {
                commitMainFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void commitMainFragment() {
        if (!fragmentManager.matchClass(fragmentManager.getCurrent(), CategoryFragment.class)) {
            fragmentManager.setPrimaryAfterPopBack(new CategoryFragment());
        }
    }

    @Override
    public void onAddAdvertClick() {
        if (PersonalData.getInstance(this).getToken() != null) {
            if (!fragmentManager.matchClass(fragmentManager.getCurrent(), AdvertListFragment.class)) {
                onCommit(AddAdvertisementFragment.newInstance(0, null), AddAdvertisementFragment.class.getCanonicalName(), null);
            } else {
                onCommit(AddAdvertisementFragment.newInstance(((AdvertListFragment) fragmentManager.getCurrent()).categoryId,
                        ((AdvertListFragment) fragmentManager.getCurrent()).categoryName), AddAdvertisementFragment.class.getCanonicalName(), null);
            }
        } else {
            Notification.getInstance().show(getApplicationContext(), getResources().getString(R.string.need_authorize));
        }
    }

    @Override
    public void onFiltersClick() {
        DrawerLayout filtersLayout = (DrawerLayout) findViewById(R.id.drawer_filters);
        if (filtersLayout != null) {
            if (filtersLayout.isDrawerVisible(END)) {
                filtersLayout.closeDrawer(END);
            } else {
                setFiltersSelected(true);
                filtersLayout.openDrawer(END);
            }
        }
    }

    @Override
    public void onDeleteClick() {
        manageHeaderIcons(HeaderIconsPolicy.SHOW_BUTTON_EDIT);
        Intent intent = new Intent(FavoritesFragment.BROADCAST_EDIT_MODE_ACTION);
        intent.putExtra(FavoritesFragment.BROADCAST_EDIT_MODE_VALUE, FavouritesMode.DELETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onEditClick() {
        manageHeaderIcons(HeaderIconsPolicy.SHOW_BUTTON_CLOSE | HeaderIconsPolicy.SHOW_BUTTON_DELETE);
        Intent intent = new Intent(FavoritesFragment.BROADCAST_EDIT_MODE_ACTION);
        intent.putExtra(FavoritesFragment.BROADCAST_EDIT_MODE_VALUE, FavouritesMode.EDIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onCloseClick() {
        manageHeaderIcons(HeaderIconsPolicy.SHOW_BUTTON_EDIT);
        Intent intent = new Intent(FavoritesFragment.BROADCAST_EDIT_MODE_ACTION);
        intent.putExtra(FavoritesFragment.BROADCAST_EDIT_MODE_VALUE, FavouritesMode.CLOSE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onMenuItemClick(int id) {
        Ui.toggleDrawer(drawerHolder);
        switch (id) {
            case R.id.iv_menu:
                Ui.toggleDrawer(drawerHolder);
                break;

            case R.id.button_menu_login:
                onCommit(new AuthorizationFragment(), AuthorizationFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_main_page:
                fragmentManager.immediatePopBack();
                commitMainFragment();
                break;

            case R.id.button_personal_account:
                onCommit(new ProfileFragment(), ProfileFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_menu_exit:
                navigationDrawer.hideTabsByAuth(false);

                new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_exit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                navigationDrawer.hideTabsByAuth(false);
                                fragmentManager.immediatePopBack();
                                commitMainFragment();

                                startService(new Intent(getApplicationContext(), ClearFavouritesService.class));

                                if (VKSdk.isLoggedIn()) VKSdk.logout();

                                GcmIntentService.unSubscribeForPush(getApplicationContext(),
                                        PersonalData.getInstance(getApplicationContext()).getToken(),
                                        PersonalData.getInstance(getApplicationContext()).getUserId());

                                onCommit(new CategoryFragment(),
                                        CategoryFragment.class.getCanonicalName(),
                                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));

                                clearPreferences();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.button_menu_messages:
                onCommit(new MessagesByUserFragment(), MessagesByUserFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
            case R.id.button_menu_favourites:
                onCommit(FavoritesFragment.newInstance(), FavoritesFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;
            case R.id.button_menu_add_advert:
                if (PersonalData.getInstance(this).getToken() != null) {
                    if (!fragmentManager.matchClass(fragmentManager.getCurrent(), AdvertListFragment.class)) {
                        onCommit(AddAdvertisementFragment.newInstance(0, null), AddAdvertisementFragment.class.getCanonicalName(), null);
                    } else {
                        onCommit(AddAdvertisementFragment.newInstance(((AdvertListFragment) fragmentManager.getCurrent()).categoryId,
                                ((AdvertListFragment) fragmentManager.getCurrent()).categoryName),
                                AddAdvertisementFragment.class.getCanonicalName(), null);
                    }
                } else {
                    Notification.getInstance().show(getApplicationContext(), getResources().getString(R.string.need_authorize));
                }
                break;
            case R.id.button_menu_notifications:
                onCommit(new NotificationFragment(), NotificationFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.button_my_adverts:
                onCommit(new MyAdvertisementsFragment(), MyAdvertisementsFragment.class.getCanonicalName(),
                        AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                break;

            case R.id.tv_copy_rights:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.main_website_url)));
                startActivity(browserIntent);
                break;
        }
    }

    private void clearPreferences() {
        startService(new Intent(getApplicationContext(), ClearNotificationsService.class));
        Preferences preferences = new Preferences(getApplicationContext(), PersonalData.PreferencesConfig.PREFERENCES_NAME);
        preferences.clearPreferences();
    }


    @Override
    protected void initSocialNetworks() {
        socialManager.addSocialNetwork(new VKNetwork());
        socialManager.addSocialNetwork(new FacebookNetwork());

    }

    private BroadcastReceiver onRequestResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_ACTION)) {
                if (intent.getBooleanExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_COMMIT_AUTHORIZATION, false)) {
                    onCommit(new AuthorizationFragment(), AuthorizationFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                }
                if (intent.getBooleanExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_RESPONSE_STATUS_BANNED, false)) {
                    requestsCount = 0;
                    progressBar.setVisibility(View.INVISIBLE);
                    new AlertDialog.Builder(Launcher.this)
                            .setTitle(com.stockroompro.api.R.string.warn)
                            .setMessage(com.stockroompro.api.R.string.warning_banned)
                            .setPositiveButton(com.stockroompro.api.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
                if (intent.getBooleanExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_RESPONSE_STATUS_NOT_ACTIVE, false)) {
                    requestsCount = 0;
                    progressBar.setVisibility(View.INVISIBLE);
                    new AlertDialog.Builder(Launcher.this)
                            .setTitle(com.stockroompro.api.R.string.warn)
                            .setMessage(com.stockroompro.api.R.string.warning_not_active)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }

                switch (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_MESSAGE_RES_ID, 0)) {
                    case com.stockroompro.api.R.string.request_social_authorization:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) != NotificationsPolicy.NOTIFY_SUCCESS) {
                            if (fragmentManager.getCurrent() != null)
                                if (fragmentManager.getCurrent() instanceof AuthorizationFragment)
                                    ((AuthorizationFragment) fragmentManager.getCurrent()).startSocialRegLoader();
                        } else {
                            GcmIntentService.subscribeForPush(context);
                            onPopBack(AbstractBasic.PopBackType.SIMPLE, null);
                            hideKeyBoard();
                        }
                        break;

                    case com.stockroompro.api.R.string.request_social_registration:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS) {
                            if (fragmentManager.getCurrent() != null)
                                if (fragmentManager.getCurrent() instanceof AuthorizationFragment)
                                    ((AuthorizationFragment) fragmentManager.getCurrent()).startSocialAuthLoader();
                            Toast.makeText(context, "Нажмите еще раз", Toast.LENGTH_LONG).show();
                        } else {
                            if (fragmentManager.getCurrent() instanceof AuthorizationFragment)
                                ((AuthorizationFragment) fragmentManager.getCurrent()).startSocialAuthLoader();

                        }
                        break;

                    case com.stockroompro.api.R.string.request_authorization:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS) {
                            setCurrentUserAsPrevious();
                            hideKeyBoard();
                        }
                        break;

                    case com.stockroompro.api.R.string.request_edit_user:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS) {
                            onPopBack(AbstractBasic.PopBackType.SIMPLE, null);
                            hideKeyBoard();
                            GcmIntentService.subscribeForPush(context);
                        }
                        break;

                    case com.stockroompro.api.R.string.request_registration:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS)
                            break;

                    case com.stockroompro.api.R.string.request_restore:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS)
                            //onPopBack(AbstractBasic.PopBackType.SIMPLE, null);
                        break;

                    case com.stockroompro.api.R.string.request_delete_profile:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS)
                            onCommit(new ProfileDeletedFragment(), ProfileDeletedFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                        break;

                    case com.stockroompro.api.R.string.request_change_password:
                        if (intent.getIntExtra(LocalBroadcastRequestProcessor.REQUEST_PROCESSOR_POLICY_ID, 0) == NotificationsPolicy.NOTIFY_SUCCESS)
                            onPopBack(AbstractBasic.PopBackType.SIMPLE, null);
                        break;
                }

            } else if (intent.getAction().equals(BackgroundUtils.REQUEST_COUNT_CHANGED_ACTION)) {
                requestsCount = intent.getBooleanExtra(BackgroundUtils.REQUEST_COUNT_VALUE, false) ? requestsCount + 1 : (requestsCount < 1 ? 0 : requestsCount - 1);
                if (requestsCount > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            } else if (intent.getAction().equals(FragmentAdvertAdded.ADVERT_CREATED)) {
                commitMainFragment();
            } else if (intent.getAction().equals(HIDE_ADD_ADVERT_PROGRESS_BAR)) {
                if (fragmentManager.matchClass(fragmentManager.getCurrent(), AddAdvertisementFragment.class) ||
                        fragmentManager.matchClass(fragmentManager.getCurrent(), EditAdvertFragment.class)) {
                    ((AddAdvertisementFragment) fragmentManager.getCurrent()).hideAddAdvertProgressBar();
                    onPopBack(AbstractBasic.PopBackType.SIMPLE, null);
                }
            }
        }
    };

    public void hideProgressBar() {
        if (progressBar != null) {
            requestsCount = 0;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setCurrentUserAsPrevious() {
        getSharedPreferences(PREVIOUS_USER_KEY, Context.MODE_PRIVATE).edit().putLong(USER_ID, PersonalData.getInstance(this).getUserId()).commit();
    }

    public void setFiltersSelected(boolean selected) {
        headerView.setFiltersSelected(selected);
        //locationSpinnerContainer.setVisibility(selected ? View.GONE : View.VISIBLE);
    }

    public void manageHeaderIcons(int strategy) {
        headerView.manageHeaderIcons(strategy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragmentFromNotification(getIntent());


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.stockroompro",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
    }

    private void showFragmentFromNotification(Intent intent) {
        if (intent != null)
            switch (intent.getIntExtra(NotificationHelper.FRAGMENT_TAG, 0)) {
                case NotificationHelper.StatusBarNotificationTypes.NEW_MESSAGE_TYPE:
                    Bundle bundle = new CommonBundleBuilder()
                            .putLong(MessageByAdvertContract.AD_ID, intent.getLongExtra(NotificationHelper.AD_TAG, 0))
                            .putString(MessageByAdvertContract.SENDER_NAME, DEFAULT_NAME)
                            .putLong(MessageByAdvertContract.RECIPIENT_ID, intent.getLongExtra(NotificationHelper.RECIPIENT_TAG, 0))
                            .build();

                    onCommit(ChatFragment.newInstance(bundle), ChatFragment.class.getCanonicalName(),
                            AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));

                    break;

                case NotificationHelper.StatusBarNotificationTypes.AD_DISMISSED_NOTIFICATION:
                    onCommit(NotificationFragment.newInstance(NotificationFragment.NotificationDataTypes.IMPORTANT),
                            NotificationFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                    break;

                case NotificationHelper.StatusBarNotificationTypes.AD_APPROVED_NOTIFICATION:
                    onCommit(NotificationFragment.newInstance(NotificationFragment.NotificationDataTypes.IMPORTANT),
                            NotificationFragment.class.getCanonicalName(), AnimationCollectionFactory.getInstance().get(AnimationCollectionFactory.Type.SLIDE));
                    break;

                default:
                    break;
            }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersId.LAUNCHER_NETWORK_COUNTRY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(this, CountryContract.CONTENT_URI, null, null,
                        null, null, new CountryRequest(this, args), new LocalBroadcastRequestProcessor(this));

            case LoadersId.LAUNCHER_NETWORK_REGION_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(this, RegionContract.CONTENT_URI, null, Config.REGION_SELECTION,
                        buildSelectionArgs(args.getLong(COUNTRY_ID)), null, new RegionRequest(this, args),
                        new LocalBroadcastRequestProcessor(this));

            case LoadersId.LAUNCHER_NETWORK_CITY_LOADER_ID:
                isLocationItemsLoading = true;
                return new RequestDescriptorWithLoadFromDB(this, CityContract.CONTENT_URI, null, Config.CITY_SELECTION,
                        buildSelectionArgs(args.getLong(REGION_ID)), null, new CityRequest(this, args),
                        new LocalBroadcastRequestProcessor(this));

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        switch (loader.getId()) {
            case LoadersId.LAUNCHER_NETWORK_COUNTRY_LOADER_ID:
            case LoadersId.LAUNCHER_NETWORK_REGION_LOADER_ID:
            case LoadersId.LAUNCHER_NETWORK_CITY_LOADER_ID:
                if (data != null) {
                    DatabaseProvider.printLogs(true);
                    DatabaseProvider.logCursor(data);
                    try {
                        ArrayList<Long> listOfIds = new ArrayList<>(locationArrayList.size());
                        for (Location location : locationArrayList) {
                            listOfIds.add(location.locationId);
                        }
                        SpinnerItem[] items = createSpinnerItem(data, listOfIds,
                                (locationArrayList.size() > 0 ? locationArrayList.get(locationArrayList.size() - 1).parentId : 0));
                        if (items.length > 1) {
                            adapter = new ActivityLauncherLocationSpinnerAdapter(this.getApplicationContext(),
                                    R.layout.advertisement_spinner_item, items, locationArrayList, this);
                            locationSpinner.setAdapter(adapter);
                            isLocationItemsLoading = false;
                        }
                    } finally {
                        data.close();
                    }
                }
                break;
        }
    }

    public SpinnerItem[] createSpinnerItem(Cursor cursor, ArrayList<Long> listOfIds, long parentId) {
        SpinnerItem[] items = new SpinnerItem[cursor.getCount() + 1];
        SpinnerItem firstItem = new SpinnerItem();
        firstItem.setId((listOfIds.size() > 0) ? (listOfIds.get(listOfIds.size() - 1)) : 0);
        firstItem.setName(PREVIOUS_ELEMENT);
        items[0] = firstItem;
        if (cursor.moveToFirst()) {
            int i = 1;
            do {
                SpinnerItem item = new SpinnerItem();
                item.setId(cursor.getLong((cursor.getColumnIndexOrThrow(CategoryColumns.ID))));
                item.setParentId(parentId);
                item.setName(cursor.getString((cursor.getColumnIndexOrThrow(CategoryColumns.NAME))));
                items[i] = item;
                i++;
            } while (cursor.moveToNext());
        }
        return items;
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public String[] buildSelectionArgs(long id) {
        return new String[]{
                String.valueOf(id),
        };
    }

    private void restartLocationLoader(int loaderId, Bundle args) {
        if (getLoaderManager().getLoader(loaderId) != null) {
            getLoaderManager().getLoader(loaderId).reset();
        }
        getLoaderManager().restartLoader(loaderId, args, this);
    }

    @Override
    public void onLocationItemClick(int position, long id) {

        if (userSearchLocationFromPreferences) {
            locationArrayList.clear();
            userSearchLocation.clear();
            userSearchLocationFromPreferences = false;
        }

        if (!isLocationItemsLoading) {
            switch (position) {
                case 0:
                    if (locationArrayList.size() > 0) {
                        switch (locationArrayList.size()) {
                            case Config.SIZE_WITH_COUNTRY_ONLY:
                                userSearchLocation.remove(0);
                                locationArrayList.remove(0);
                            case Config.SIZE_WITH_COUNTRY_AND_REGION:
                                userSearchLocation.clear();
                                locationArrayList.clear();
                                locationType = Config.TYPE_COUNTRY;
                                restartLocationLoader(LoadersId.LAUNCHER_NETWORK_COUNTRY_LOADER_ID, null);
                                break;

                            case Config.SIZE_WITH_COUNTRY_REGION_CITY:
                                locationType = Config.TYPE_REGION;
                                userSearchLocation.remove(userSearchLocation.size() - 1);
                                locationArrayList.remove(locationArrayList.size() - 1);
                                Bundle regionArgs = new Bundle();
                                regionArgs.putLong(COUNTRY_ID, locationArrayList.get(1).parentId);
                                restartLocationLoader(LoadersId.LAUNCHER_NETWORK_REGION_LOADER_ID, regionArgs);
                                break;
                        }
                    } else {
                        locationType = Config.TYPE_COUNTRY;
                        restartLocationLoader(LoadersId.LAUNCHER_NETWORK_COUNTRY_LOADER_ID, null);
                    }
                    break;

                default:
                    switch (locationType) {
                        case Config.TYPE_COUNTRY:
                            Location country = new Location(id, adapter.getLocationName(position), 0, Config.TYPE_COUNTRY);
                            locationType = Config.TYPE_REGION;
                            locationArrayList.add(country);
                            userSearchLocation.add(country.toJson());
                            Bundle countryArgs = new Bundle();
                            countryArgs.putLong(COUNTRY_ID, id);
                            restartLocationLoader(LoadersId.LAUNCHER_NETWORK_REGION_LOADER_ID, countryArgs);
                            break;

                        case Config.TYPE_REGION:
                            if (locationArrayList.size() == 2) {
                                userSearchLocation.remove(locationArrayList.get(locationArrayList.size() - 1).toJson());
                                locationArrayList.remove(locationArrayList.size() - 1);
                            }
                            Location region = new Location(id, adapter.getLocationName(position),
                                    locationArrayList.get(0).locationId, Config.TYPE_REGION);

                            locationType = Config.TYPE_CITY;
                            locationArrayList.add(region);
                            userSearchLocation.add(region.toJson());
                            Bundle regionArgs = new Bundle();
                            regionArgs.putLong(COUNTRY_ID, locationArrayList.get(0).locationId);
                            regionArgs.putLong(REGION_ID, id);
                            restartLocationLoader(LoadersId.LAUNCHER_NETWORK_CITY_LOADER_ID, regionArgs);
                            break;

                        case Config.TYPE_CITY:
                            if (locationArrayList.size() == 3) {
                                userSearchLocation.remove(locationArrayList.get(locationArrayList.size() - 1).toJson());
                                locationArrayList.remove(locationArrayList.size() - 1);
                            }
                            Location city = new Location(id, adapter.getLocationName(position),
                                    locationArrayList.get(1).locationId, Config.TYPE_CITY);
                            locationArrayList.add(city);
                            userSearchLocation.add(city.toJson());
                            locationSpinner.setSelection(position);
                            SystemHelper.getInstance().closeSpinner(locationSpinner);
                            break;
                    }
                    break;
            }
            PersonalData.getInstance(this).removeUserSearchLocation();
            PersonalData.getInstance(this).setUserSearchLocation(userSearchLocation);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_launcher_button_confirm:
                if (fragmentManager.matchClass(fragmentManager.getCurrent(), AdvertListFragment.class)) {
                    Log.e(Launcher.class.getSimpleName(), "updateList");
                    ((AdvertListFragment) fragmentManager.getCurrent()).updateList();
                }
                if (fragmentManager.matchClass(fragmentManager.getCurrent(), SearchResultsFragment.class)) {
                    ((SearchResultsFragment) fragmentManager.getCurrent()).startSearch();
                }
                break;
        }
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        decrypt();
    }

    //TODO REMOVE METHOD BEFORE PUBLISH!!!
    private void decrypt() {
        File encryptedFile = getApplicationContext().getDatabasePath(DatabaseConfig.NAME);
        String decryptedDatabaseName = "decrypted_" + DatabaseConfig.NAME;
        File decryptedFile = getApplicationContext().getDatabasePath(decryptedDatabaseName);
        decryptedFile.delete();

        String signature = SystemHelper.getInstance().getAppSignature();
        if (encryptedFile.exists()) {
            SQLiteDatabase encryptedDatabase = SQLiteDatabase.openOrCreateDatabase(encryptedFile, signature, null, null);
            if (encryptedDatabase.isOpen()) {
                encryptedDatabase.rawExecSQL("PRAGMA key = '" + signature + "';");
                encryptedDatabase.rawExecSQL("ATTACH DATABASE '" + decryptedFile.getAbsolutePath() + "' as decrypted KEY '';");
                encryptedDatabase.rawExecSQL("SELECT sqlcipher_export('decrypted');");
                encryptedDatabase.rawExecSQL("DETACH DATABASE decrypted;");

                SQLiteDatabase decryptedDatabase = SQLiteDatabase.openOrCreateDatabase(decryptedFile.getAbsolutePath(), "", null);
                decryptedDatabase.close();
                encryptedDatabase.close();
            }
        }
    }*/
}