package com.mirhoseini.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    static String deviceInfo = "";
    static enumDensity density = null;
    static HashMap<String, String> deviceInfoParams;

    /**
     * Copy an InputStream to an OutputStream
     *
     * @param is InputStream
     * @param os OutputStream
     */
    public static void CopyStream(InputStream is, OutputStream os) throws IOException {
        final int buffer_size = 1024;
        byte[] bytes = new byte[buffer_size];
        for (; ; ) {
            int count = is.read(bytes, 0, buffer_size);
            if (count == -1)
                break;
            os.write(bytes, 0, count);
        }
    }

    /**
     * Convert an integer array to csv format
     *
     * @param array Input array
     * @return csv
     */
    public static String intArrayToCSV(int[] array) {
        String result = "";
        for (int item : array) {
            if (result != "")
                result += ",";
            result += String.valueOf(item);
        }
        return result;
    }

    /**
     * Exit with kill process
     */
    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * Exit application
     *
     * @param activity
     */
    public static void exit(Activity activity) {
        exit((Context) activity);
        System.exit(0);

    }

    /**
     * Exit with finish context
     *
     * @param context Context of activity of an activity
     */
    public static void exit(Context context) {
        Activity activity = (Activity) context;
        activity.finish();
    }

    /**
     * Open website with a URL in resources
     *
     * @param context  Application context
     * @param resource Resource of string of a link
     * @throws NotFoundException
     */
    public static void openWebsite(Context context, int resource)
            throws NotFoundException {
        openWebsite(context, context.getResources().getString(resource));
    }

    /**
     * Open website with a string URL
     *
     * @param context Application context
     * @param url     link to open
     */
    public static void openWebsite(Context context, String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        context.startActivity(webIntent);
    }

    /**
     * Get Device model
     *
     * @return
     */
    public static String getMobileModel() {
        // Device model
        return Build.MODEL;
    }

    /**
     * Get Device Manufacturer
     *
     * @return
     */
    public static String getMobileManufacturer() {
        // Device model
        return Build.MANUFACTURER;
    }

    /**
     * Get Device product
     *
     * @return
     */
    public static String getMobileProduct() {
        // Device model
        return Build.PRODUCT;
    }

    /**
     * Get Device fingerprint
     *
     * @return
     */
    public static String getMobileFingerprint() {
        // Device model
        return Build.FINGERPRINT;
    }

    /**
     * Get Device ID
     *
     * @return
     */
    public static String getMobileId() {
        // Device model
        return Build.ID;
    }

    /**
     * Get Device Android version
     *
     * @return
     */
    public static String getAndroidVersion() {
        // Android version
        return Build.VERSION.RELEASE;
    }

    /**
     * Get Device Android version integer
     *
     * @return
     */
    public static int getAndroidVersionInt() {
        // Android version
        return Build.VERSION.SDK_INT;
    }

    /**
     * Check if network is available
     *
     * @param context Application context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * Encode boolean param in URL to make it use to call in web api
     *
     * @param data Boolean parameter
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeURL(boolean data)
            throws UnsupportedEncodingException {
        if (data)
            return encodeURL("true");
        else
            return encodeURL("false");
    }

    /**
     * Encode integer param in URL to make it use to call in web api
     *
     * @param data Integer parameter
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeURL(int data)
            throws UnsupportedEncodingException {
        return encodeURL(String.valueOf(data));
    }

    /**
     * Encode string param in URL to make it use to call in web api
     *
     * @param data String parameter
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeURL(String data)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(data, "UTF8");
    }

    /**
     * Check if an application is installed or not using Package Name
     *
     * @param context     Application context
     * @param packageName Package name to check
     * @return
     */
    public static Boolean isInstalled(Context context, String packageName) {

        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);

        if (intent == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Showing alert dialog when network is not available
     *
     * @param context              application context
     * @param isConnectionCritical if network is necessary dialog will show and is not cancelable
     * @return
     */
    public static AlertDialog showNoInternetConnectionDialog(
            final Context context, final boolean isConnectionCritical) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(R.string.utils__no_connection);
        builder.setTitle(R.string.utils__no_connection_title);
        builder.setPositiveButton(R.string.utils__settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(
                                Settings.ACTION_WIFI_SETTINGS));// .ACTION_NETWORK_OPERATOR_SETTINGS));//.ACTION_WIRELESS_SETTINGS));
                    }
                });

        if (isConnectionCritical)
            builder.setNegativeButton(R.string.utils__exit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Utils.exit(context);
                        }
                    });
        else
            builder.setNegativeButton(R.string.utils__abort, null);

        if (isConnectionCritical)
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    Utils.exit(context);
                }
            });

        return builder.show();
    }

    /**
     * Showing alert dialog when network is not available in offline mode
     *
     * @param context              application context
     * @param isConnectionCritical It means if network is necessary dialog will show and cancelable will be false
     * @return
     */
    public static AlertDialog showNoInternetConnectionDialogOfflineMsg(
            final Context context, final boolean isConnectionCritical) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(R.string.utils__no_connection);
        builder.setTitle(R.string.utils__no_connection_title);
        builder.setPositiveButton(R.string.utils__settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(
                                Settings.ACTION_WIFI_SETTINGS));// .ACTION_NETWORK_OPERATOR_SETTINGS));//.ACTION_WIRELESS_SETTINGS));
                    }
                });

        if (isConnectionCritical)
            builder.setNegativeButton(R.string.utils__exit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Utils.exit(context);
                        }
                    });
        else
            builder.setNegativeButton(R.string.utils__offline, null);

        if (isConnectionCritical)
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    Utils.exit(context);
                }
            });

        return builder.show();
    }

    /**
     * Showing alert dialog when server is not available
     *
     * @param context    Application context
     * @param isCritical It means if server connection is necessary dialog will show and cancelable will be false
     * @return
     */
    public static AlertDialog showServerNotAvailableDialog(
            final Context context, final boolean isCritical) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(R.string.utils__no_server);
        builder.setTitle(R.string.utils__no_server_title);

        if (isCritical)
            builder.setNegativeButton(R.string.utils__exit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.exit(context);
                        }
                    });
        else
            builder.setNegativeButton(R.string.utils__abort, null);

        if (isCritical)
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    Utils.exit(context);
                }
            });

        return builder.show();
    }

    /**
     * Changing default language
     *
     * @param context       Application context
     * @param language_code Lang code to FA or EN - BR and etc.
     * @param title         Will set to activity
     */
    public static void changeLanguage(Context context, String language_code,
                                      String title) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code);
        res.updateConfiguration(conf, dm);

        Activity activity = (Activity) context;
        activity.setTitle(title);
    }

    /**
     * Changing default language
     *
     * @param context       Application context
     * @param language_code Lang code to FA or EN - BR and etc.
     */
    public static void changeLanguage(Context context, String language_code) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code);
        res.updateConfiguration(conf, dm);
    }

    /**
     * Make hidden keyboard of a edit text
     *
     * @param context Application context
     * @param et      Edit text that you want hide the keyboard
     */
    public static void hideKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * Get Display Width
     *
     * @param context Application context
     * @return
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getDisplayWidth(Context context) {
        Activity activity = (Activity) context;
        if (Integer.valueOf(Build.VERSION.SDK_INT) < 13) {
            Display display = activity.getWindowManager()
                    .getDefaultDisplay();
            return display.getWidth();
        } else {
            Display display = activity.getWindowManager()
                    .getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
    }

    /**
     * Get display height
     *
     * @param context Application context
     * @return
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getDisplayHeight(Context context) {
        Activity activity = (Activity) context;
        if (Integer.valueOf(Build.VERSION.SDK_INT) < 13) {
            Display display = activity.getWindowManager()
                    .getDefaultDisplay();
            return display.getHeight();
        } else {
            Display display = activity.getWindowManager()
                    .getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
    }

    /**
     * Play a sound
     *
     * @param context Application context
     * @param rawID   Raw integer id in resource
     */
    public static void playSound(Context context, int rawID) {
        MediaPlayer mp = MediaPlayer.create(context, rawID);
        mp.start();
    }

    /**
     * Get Application name
     *
     * @param context Application context
     * @return
     * @throws NameNotFoundException
     */
    public static String getApplicationName(Context context)
            throws NameNotFoundException {
        // Application version
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0);
        return pInfo.packageName;

    }

    /**
     * Get Application version name
     *
     * @param context Application context
     * @return
     * @throws NameNotFoundException
     */
    public static String getApplicationVersionName(Context context) throws NameNotFoundException {
        // Application version
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0);
        return pInfo.versionName;
    }

    /**
     * Get application version code
     *
     * @param context Application context
     * @return
     * @throws NameNotFoundException
     */
    public static int getApplicationVersionCode(Context context) throws NameNotFoundException {
        // Application version
        PackageInfo pInfo = null;
        pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionCode;
    }

    /**
     * Get android ID
     *
     * @param context Application context
     * @return
     */
    public static String getAndroidID(Context context) {

        String m_szAndroidID = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    /**
     * Play notification sound
     *
     * @param context Application context
     */
    public static void playNotificationSound(Context context) {
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    /**
     * Checking a service is running or not
     *
     * @param context   Application context
     * @param myService Set your service class
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> myService) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (myService.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checking that a national code is in a correct format or not
     *
     * @param melliCode Getting national code
     * @return
     */
    public static boolean checkIranianNationalityCode(String melliCode) {
        try {
            melliCode = melliCode.replaceAll("-", "");
            if (melliCode.length() == 10) {
                if (melliCode.equals("0000000000")
                        || melliCode.equals("1111111111")
                        || melliCode.equals("2222222222")
                        || melliCode.equals("3333333333")
                        || melliCode.equals("4444444444")
                        || melliCode.equals("5555555555")
                        || melliCode.equals("6666666666")
                        || melliCode.equals("7777777777")
                        || melliCode.equals("8888888888")
                        || melliCode.equals("9999999999"))
                    return false;

                int c = Integer.valueOf(melliCode.charAt(9) + "");
                int n = Integer.valueOf(melliCode.charAt(0) + "") * 10
                        + Integer.valueOf(melliCode.charAt(1) + "") * 9
                        + Integer.valueOf(melliCode.charAt(2) + "") * 8
                        + Integer.valueOf(melliCode.charAt(3) + "") * 7
                        + Integer.valueOf(melliCode.charAt(4) + "") * 6
                        + Integer.valueOf(melliCode.charAt(5) + "") * 5
                        + Integer.valueOf(melliCode.charAt(6) + "") * 4
                        + Integer.valueOf(melliCode.charAt(7) + "") * 3
                        + Integer.valueOf(melliCode.charAt(8) + "") * 2;
                int r = n - (n / 11) * 11;

                if ((r == 0 && r == c) || (r == 1 && c == 1)
                        || (r > 1 && c == 11 - r))
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checking a email that user entered is in a correct format or not
     *
     * @param email Email parameter
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Put fake sms in inbox of device
     *
     * @param context Application context
     * @param address Phone number
     * @param body    SMS Body
     */
    public static void putSMS(Context context, String address, String body) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("body", body);
        // values.put("read", read);
        // values.put("date", date);
        context.getContentResolver().insert(Uri.parse("content://sms/inbox"),
                values);
    }

    /**
     * Put fake MMS in inbox of device
     *
     * @param context Application context
     * @param address Phone number
     * @param body    MMS Body
     */
    public static void putMMS(Context context, String address, String body) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("body", body);
        // values.put("read", read);
        // values.put("date", date);
        context.getContentResolver().insert(Uri.parse("content://mms/inbox"),
                values);
    }

    /**
     * Getting a full list of data from device
     *
     * @param context Application context
     * @return
     * @throws UnsupportedEncodingException
     * @throws NameNotFoundException
     */
    public static HashMap<String, String> getDeviceInfoParamsForUrl(Context context) throws UnsupportedEncodingException, NameNotFoundException {
        if (deviceInfoParams == null) {
            deviceInfoParams = new HashMap<>();

            deviceInfoParams.put("androidVersionName", Utils.getAndroidVersion());
            deviceInfoParams.put("androidVersionId", Utils.getAndroidVersionInt() + "");
            deviceInfoParams.put("androidId", Utils.getAndroidID(context));
            deviceInfoParams.put("mobileModel", Utils.getMobileModel());
            deviceInfoParams.put("mobileManufacturer", Utils.getMobileManufacturer());
            deviceInfoParams.put("mobileId", Utils.getMobileId());
            deviceInfoParams.put("mobileProduct", Utils.getMobileProduct());
            deviceInfoParams.put("applicationName", Utils.getApplicationName(context));
            deviceInfoParams.put("applicationVersionName", Utils.getApplicationVersionName(context));
            deviceInfoParams.put("applicationVersionCode", Utils.getApplicationVersionCode(context) + "");
            deviceInfoParams.put("screenWidth", Utils.getDisplayWidth(context) + "");
            deviceInfoParams.put("screenHeight", Utils.getDisplayWidth(context) + "");
            deviceInfoParams.put("screenDensity", Utils.getDisplayDensity(context) + "");
            deviceInfoParams.put("screenDensityName", Utils.getDisplaySize(context).toString());
            deviceInfoParams.put("atdPackages", Utils.getKarinaPackages(context));
        }
        return new HashMap<>(deviceInfoParams);
    }

    /**
     * Get Display density
     *
     * @param context Application context
     * @return
     */
    public static int getDisplayDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.densityDpi;
    }

    /**
     * Get display size
     *
     * @param context Application context
     * @return
     */
    public static enumDensity getDisplaySize(Context context) {
        if (density == null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            density = enumDensity.fromFloat(metrics.density);
        }
        return density;
    }

    /**
     * Get all applications of karina in device
     *
     * @param context Application context
     * @return
     */
    public static String getKarinaPackages(Context context) {
        final PackageManager pm = context.getPackageManager();
        // get a list of installed apps.
        List<PackageInfo> packages = pm
                .getInstalledPackages(PackageManager.GET_META_DATA);
        // .getInstalledApplications(PackageManager.GET_META_DATA);

        String result = "";

        for (PackageInfo packageInfo : packages) {
            if (packageInfo.packageName.contains("karina")) {
                if (TextUtils.isEmpty(result))
                    result = packageInfo.packageName + "("
                            + packageInfo.versionCode + ")";
                else
                    result += ", " + packageInfo.packageName + "("
                            + packageInfo.versionCode + ")";
            }
        }
        return result;
    }

    /**
     * Making a string of currency and convert it to a format with separators
     *
     * @param value
     * @return
     */
    public static String moneySeparator(String value) {
        return moneySeparator(value, ",");
    }

    /**
     * Making a string of currency and convert it to a format with separators with specific separator
     *
     * @param value     Money value
     * @param separator Your specific separator
     * @return
     */
    public static String moneySeparator(String value, String separator) {
        String result = "";
        int len = value.length();
        int loop = (len / 3);

        int start = 0;
        int end = len - (loop * 3);

        result = value.substring(start, end);

        for (int i = 0; i < loop; i++) {
            start = end;
            end += 3;
            if (result.equals(""))
                result = value.substring(start, end);
            else
                result = result + separator + value.substring(start, end);
        }

        return result;
    }

    /**
     * Making empty all application data in cache
     *
     * @param context Application context
     * @return
     */
    public static Boolean emptyAllApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());

        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("databases")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }

        return true;
    }

    /**
     * Delete directory and subdirectory
     *
     * @param dir File dir address
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir != null & dir.isDirectory()) {
            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                    return false;
            }
        }
        return dir.delete();
    }

    /**
     * Checking install package permission
     *
     * @param context Application context
     * @return
     */
    public static boolean checkInstallPackagesPermission(Context context) {
        String permission = "android.permission.INSTALL_PACKAGES";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Checking write sms permission
     *
     * @param context Application context
     * @return
     */
    public static boolean checkWriteSMSPermission(Context context) {
        String permission = "android.permission.WRITE_SMS";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Checking read sms permission
     *
     * @param context Application context
     * @return
     */
    public static boolean checkReadSMSPermission(Context context) {
        String permission = "android.permission.READ_SMS";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Checking if a string number or not
     *
     * @param value string value
     * @return
     */
    public static boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Getting all installed packages
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllInstalledApplication(Context context) {
        final PackageManager pm = context.getPackageManager();
        // get a list of installed apps.
        List<PackageInfo> packages = pm
                .getInstalledPackages(PackageManager.GET_META_DATA);

        return packages;
    }

    public static String getFrontPackageName(Context ctx) {

        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        String str = "";
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();

        str = processes.get(0).processName;

        return str;
    }

    public static boolean isMyServiceRunning(Context ctx, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equalsIgnoreCase(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMyServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * check if current device is Tablet
     *
     * @param context Application context
     * @return isTablet
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * check if current device state is Portrait
     *
     * @param context Application context
     * @return isPortrait
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * get device current timestamp
     *
     * @return isPortrait
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Enumeration of standard density of displays
     */
    public static enum enumDensity {
        xxxhdpi(4.0f), xxhdpi(3.0f), xhdpi(2.0f), hdpi(1.5f), tvdpi(1.33f), mdpi(
                1.0f), ldpi(0.75f);

        private Float value;

        enumDensity(Float v) {
            setValue(v);
        }

        public static enumDensity fromFloat(Float v) {
            if (v != null) {
                for (enumDensity s : enumDensity.values()) {
                    if (v.equals(s.getValue())) {
                        return s;
                    }
                }
                return enumDensity.xxxhdpi;
            }
            return null;
        }

        public Float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }
    }

    /**
     * Apply typeface to a plane text and return spannableString
     *
     * @param text Text that you want to apply typeface
     * @param typeface Typeface that you want to apply to your text
     * @return spannableString
     */
    public static SpannableString applyTypefaceToString(String text, final Typeface typeface) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new MetricAffectingSpan() {
                                    @Override
                                    public void updateMeasureState(TextPaint p) {
                                        p.setTypeface(typeface);

                                        // Note: This flag is required for proper typeface rendering
                                        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint tp) {
                                        tp.setTypeface(typeface);

                                        // Note: This flag is required for proper typeface rendering
                                        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
                                    }
                                }, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}