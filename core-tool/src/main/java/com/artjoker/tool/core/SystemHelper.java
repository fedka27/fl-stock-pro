package com.artjoker.tool.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

import static android.content.Context.WINDOW_SERVICE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

//TODO REMOVE BEFORE RELEASE!!!
//  keystore:   SHA1
//  debug:      BC747E681C531B18B1C82E0B81C5B80230E5046E    Mikle
//  debug:      0A0E76C3ECB19EB61F4BBE4226C6F4CEAFE38A1B    Anastasia
//  debug:      96F9EAB2253873031BAD2255CCC098710125E2D9    Alexandr
//  release:    AE381608DE92BB883B021D92871A4B0D482BDD30
public final class SystemHelper {
    private final Calendar calendar;
    private Context context;

    {
        calendar = Calendar.getInstance(SimpleTimeZone.getDefault());
        calendar.setTimeInMillis(currentTimeMillis());
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
    }

    private SystemHelper() {

    }

    public static SystemHelper getInstance() {
        return SystemHelperHolder.INSTANCE;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public WindowManager getWindowManager(final Context context) {
        return ((WindowManager) context.getApplicationContext().getSystemService(WINDOW_SERVICE));
    }

    public Intent buildIntent(final Context context, final Class cls) {
        return new Intent(context.getApplicationContext(), cls);
    }

    public void startForResult(final Context context, final Intent intent, final int requestCode) {
        if (context instanceof Activity) {
            startForResult((Activity) context, intent, requestCode);
        }
    }

    public void startForResult(final Activity activity, final Intent intent, final int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public boolean queryParameterExist(final Uri uri, final String key) {
        return (uri.getQueryParameter(key) != null);
    }

    public boolean exist(final Object o) {
        return (o != null);
    }

    public boolean arrayExist(final Object[] array) {
        return exist(array) && notEmpty(array.length);
    }

    public boolean listExist(final List list) {
        return exist(list) && notEmpty(list.size());
    }

    public boolean notEmpty(final int size) {
        return (size > 0);
    }

    public boolean positive(final long value) {
        return (value > 0);
    }

    public String getAppSignature() {
        try {
            if (exist(context)) {
                final Signature[] signatures = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
                if (arrayExist(signatures)) {
                    return getSha(signatures[0].toByteArray());
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSha(final byte[] signature) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.update(signature);
        return bytesToHex(messageDigest.digest());
    }

    private String bytesToHex(final byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis() + SimpleTimeZone.getDefault().getRawOffset();
    }

    public long currentTime() {
        return currentTimeMillis() / 1000;
    }

    public long currentDateTime() {
        return calendar.getTimeInMillis();
    }

    public boolean compareCalendarMonth(MonthDisplayHelper monthDisplayHelper) {
        if (monthDisplayHelper.getYear() < calendar.get(YEAR)) {
            return true;
        } else {
            if (monthDisplayHelper.getMonth() < calendar.get(MONTH)) {
                return true;
            }
        }
        return false;
    }

    public Calendar dateCalendar(long timeMillis) {
        Calendar calendar = Calendar.getInstance(SimpleTimeZone.getDefault());
        calendar.setTimeInMillis(timeMillis);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar;
    }

    public long dateTime(long timeMillis) {
        return dateCalendar(timeMillis).getTimeInMillis();
    }

    public long nextHourDelay() {
        long currentTime = currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(HOUR_OF_DAY, calendar.get(HOUR_OF_DAY) + 1);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTimeInMillis() - currentTime;
    }

    public long getTimeInMillisFromSec(long time) {
        return time * 1000;
    }

    public long getTimeInSecFromMillis(long time) {
        return time / 1000;
    }

    public String getUniqueDeviceId() {
        return (exist(context)) ? Crypto.md5(getBuildId() + getWifiMac()) : null;
    }

    private String getBuildId() {
        return "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
    }

    private String getWifiMac() {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }

    public void closeSpinner(View view) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SystemHelperHolder {
        private static final SystemHelper INSTANCE = new SystemHelper();
    }

}