package com.stockroompro.utils;

import android.content.Context;
import android.os.Bundle;

import com.artjoker.core.BundleBuilder;
import com.artjoker.core.CommonBundleBuilder;
import com.stockroompro.api.models.requests.RequestParams;
import com.stockroompro.fragments.AddAdvertisementFragment;
import com.stockroompro.models.PersonalData;
import com.stockroompro.models.spinners.Location;

import java.util.ArrayList;

/**
 * Created by Alexandr.Bagach on 21.10.2015.
 */
public class AddUserLocationIntoBundle {

    private Context context;

    private static class AddUserLocationIntoBundleHolder {
        private static final AddUserLocationIntoBundle INSTANCE = new AddUserLocationIntoBundle();
    }

    private AddUserLocationIntoBundle() {}

    public static AddUserLocationIntoBundle getInstance() {
        return AddUserLocationIntoBundleHolder.INSTANCE;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public Bundle addLocation(Bundle bundle) {
        ArrayList<String> userSearchLocation = PersonalData.getInstance(context).getUserSearchLocation();
        ArrayList<Location> list = new ArrayList<>();
        for (String item : userSearchLocation) {
            list.add(new Location(item));
        }

        switch (list.size()) {
            case 1:
                bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(0).locationId);
                break;

            case 2:
                if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(0).locationId);
                } else if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(0).locationId);
                }

                if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(1).locationId);
                } else if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(1).locationId);
                }
                break;

            case 3:
                if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(0).locationId);
                } else if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(0).locationId);
                } else if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_CITY) {
                    bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, list.get(0).locationId);
                }

                if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(1).locationId);
                } else if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(1).locationId);
                } else if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_CITY) {
                    bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, list.get(1).locationId);
                }

                if (list.get(2).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(2).locationId);
                } else if (list.get(2).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(2).locationId);
                } else if (list.get(2).locationType == AddAdvertisementFragment.Config.TYPE_CITY) {
                    bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, list.get(2).locationId);
                }
                break;
        }
        return bundle;
    }
    public Bundle addLocationWithDef(Bundle bundle) {
        ArrayList<String> userSearchLocation = PersonalData.getInstance(context).getUserSearchLocation();
        ArrayList<Location> list = new ArrayList<>();
        for (String item : userSearchLocation) {
            list.add(new Location(item));
        }

        switch (list.size()) {
            case 1:
                bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(0).locationId);
                break;

            case 2:
                if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(0).locationId);
                } else if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(0).locationId);
                }

                if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(1).locationId);
                } else if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(1).locationId);
                }
                break;

            case 3:
                if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(0).locationId);
                } else if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(0).locationId);
                } else if (list.get(0).locationType == AddAdvertisementFragment.Config.TYPE_CITY) {
                    bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, list.get(0).locationId);
                }

                if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(1).locationId);
                } else if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(1).locationId);
                } else if (list.get(1).locationType == AddAdvertisementFragment.Config.TYPE_CITY) {
                    bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, list.get(1).locationId);
                }

                if (list.get(2).locationType == AddAdvertisementFragment.Config.TYPE_COUNTRY) {
                    bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, list.get(2).locationId);
                } else if (list.get(2).locationType == AddAdvertisementFragment.Config.TYPE_REGION) {
                    bundle.putLong(RequestParams.ParamNames.REGION_ID, list.get(2).locationId);
                } else if (list.get(2).locationType == AddAdvertisementFragment.Config.TYPE_CITY) {
                    bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, list.get(2).locationId);
                }
                break;
            default:
                bundle.putLong(RequestParams.ParamNames.COUNTRY_ID, -1L);
                bundle.putLong(RequestParams.ParamNames.REGION_ID, -1L);
                bundle.putLong(RequestParams.ParamNames.ADVERT_CITY_ID, -1L);
        }
        return bundle;
    }
}
