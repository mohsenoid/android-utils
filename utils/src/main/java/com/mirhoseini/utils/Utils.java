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
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class Utils {

    static String deviceInfo = "";
    static List<BasicNameValuePair> deviceInfoParams = null;
    static enumDensity density = null;

    /**
     * Copy an input stream to an output stream
     *
     * @param is Input
     * @param os Output
     */
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Convert an array to csv format
     *
     * @param array Input array
     * @return
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
        try {
            Activity activity = (Activity) context;
            activity.finish();
            // System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open website with one link in resource
     *
     * @param context  Context of application
     * @param resource Resource of string of a link
     * @throws NotFoundException
     */
    public static void openWebsite(Context context, int resource)
            throws NotFoundException {
        openWebsite(context, context.getResources().getString(resource));
    }

    /**
     * Open website with link string
     *
     * @param context Context of application
     * @param url     link to open
     */
    public static void openWebsite(Context context, String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        context.startActivity(webIntent);
    }

    /**
     * Get mobile model
     *
     * @return
     */
    public static String getMobileModel() {
        // Device model
        return Build.MODEL;
    }

    /**
     * Get Mobile Manufacturer
     *
     * @return
     */
    public static String getMobileManufacturer() {
        // Device model
        return Build.MANUFACTURER;
    }

    /**
     * Get mobile produce
     *
     * @return
     */
    public static String getMobileProduct() {
        // Device model
        return Build.PRODUCT;
    }

    /**
     * Get mobile fingerprint
     *
     * @return
     */
    public static String getMobileFingerprint() {
        // Device model
        return Build.FINGERPRINT;
    }

    /**
     * Get mobile ID
     *
     * @return
     */
    public static String getMobileId() {
        // Device model
        return Build.ID;
    }

    /**
     * Get android version
     *
     * @return
     */
    public static String getAndroidVersion() {
        // Android version
        return Build.VERSION.RELEASE;
    }

    /**
     * Get android version integer
     *
     * @return
     */
    public static int getAndroidVersionInt() {
        // Android version
        return Build.VERSION.SDK_INT;
    }

    /**
     * Checking network or internet is available
     *
     * @param context Context of application
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
     * Encode url to make it use to call in web api
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
     * Encode url to make it use to call in web api
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
     * Encode url to make it use to call in web api
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
     * Check a package is installed in device or not
     *
     * @param context     Context of application
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
     * @param context    Context of application
     * @param isCritical It means if network is necessary dialog will show and cancelable will be false
     * @return
     */
    public static AlertDialog showNoInternetConnectionDialog(
            final Context context, final boolean isCritical) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage(R.string.no_connection);
            builder.setTitle(R.string.no_connection_title);
            builder.setPositiveButton(R.string.settings,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(
                                    Settings.ACTION_WIFI_SETTINGS));// .ACTION_NETWORK_OPERATOR_SETTINGS));//.ACTION_WIRELESS_SETTINGS));
                        }
                    });

            if (isCritical)
                builder.setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Utils.exit(context);
                            }
                        });
            else
                builder.setNegativeButton(R.string.abort, null);

            if (isCritical)
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        Utils.exit(context);
                    }
                });

            return builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Showing alert dialog when network is not available in offline mode
     *
     * @param context    Context of application
     * @param isCritical It means if network is necessary dialog will show and cancelable will be false
     * @return
     */
    public static AlertDialog showNoInternetConnectionDialogOfflineMsg(
            final Context context, final boolean isCritical) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage(R.string.no_connection);
            builder.setTitle(R.string.no_connection_title);
            builder.setPositiveButton(R.string.settings,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(
                                    Settings.ACTION_WIFI_SETTINGS));// .ACTION_NETWORK_OPERATOR_SETTINGS));//.ACTION_WIRELESS_SETTINGS));
                        }
                    });

            if (isCritical)
                builder.setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Utils.exit(context);
                            }
                        });
            else
                builder.setNegativeButton(R.string.offline, null);

            if (isCritical)
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        Utils.exit(context);
                    }
                });

            return builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Showing alert dialog when server is not available
     *
     * @param context    Context of application
     * @param isCritical It means if server connection is necessary dialog will show and cancelable will be false
     * @return
     */
    public static AlertDialog showServerNotAvailableDialog(
            final Context context, final boolean isCritical) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(R.string.no_server);
        builder.setTitle(R.string.no_server_title);

        if (isCritical)
            builder.setNegativeButton(R.string.exit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.exit(context);
                        }
                    });
        else
            builder.setNegativeButton(R.string.abort, null);

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
     * @param context       Context of application
     * @param language_code Lang code -> FA or EN - BR and etc.
     * @param title         Will set to activity
     */
    public static void changeLanguage(Context context, String language_code,
                                      String title) {
        try {
            Resources res = context.getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale(language_code);
            res.updateConfiguration(conf, dm);

            Activity activity = (Activity) context;
            activity.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changing default language
     *
     * @param context       Context of application
     * @param language_code Lang code -> FA or EN - BR and etc.
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
     * @param context Context of application
     * @param et      Edit text that you want hide the keyboard
     */
    public static void hideKeyboard(Context context, EditText et) {
        // ((Activity) context).getWindow().setSoftInputMode(
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * Get Display Width
     *
     * @param context Context of application
     * @return
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getDisplayWidth(Context context) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get display height
     *
     * @param context Context of application
     * @return
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getDisplayHeight(Context context) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Play a sound
     *
     * @param context Context of application
     * @param rawID   Raw integer id in resource
     */
    public static void playSound(Context context, int rawID) {
        MediaPlayer mp = MediaPlayer.create(context, rawID);
        mp.start();
    }

    /**
     * Get Application name
     *
     * @param context Context of application
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
     * @param context Context of application
     * @return
     * @throws NameNotFoundException
     */
    public static String getApplicationVersionName(Context context)
            throws NameNotFoundException {
        // Application version
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0);
        return pInfo.versionName;
    }

    /**
     * Get application version codde
     *
     * @param context Context of application
     * @return
     * @throws NameNotFoundException
     */
    public static int getApplicationVersionCode(Context context) {
        // Application version
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return pInfo.versionCode;
        }

    }

    /**
     * Get android ID
     *
     * @param context Context of application
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
     * @param context Context of application
     */
    public static void playNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
        }
    }

    /**
     * Checking a service is running or not
     *
     * @param context   Context of application
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
    public static boolean checkNationalCode(String melliCode) {
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
            e.printStackTrace();
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
     * @param context Context of application
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
     * @param context Context of application
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
     * @param context Context of application
     * @return
     * @throws UnsupportedEncodingException
     * @throws NameNotFoundException
     */
    public static List<BasicNameValuePair> getDeviceInfoParamsForUrl(
            Context context) throws UnsupportedEncodingException,
            NameNotFoundException {
        if (deviceInfoParams == null) {
            deviceInfoParams = new ArrayList<BasicNameValuePair>();

            BasicNameValuePair androidVersionName = new BasicNameValuePair(
                    "androidVersionName", Utils.getAndroidVersion());
            deviceInfoParams.add(androidVersionName);

            BasicNameValuePair androidVersionId = new BasicNameValuePair(
                    "androidVersionId", Utils.getAndroidVersionInt() + "");
            deviceInfoParams.add(androidVersionId);

            BasicNameValuePair androidId = new BasicNameValuePair("androidId",
                    Utils.getAndroidID(context));
            // BasicNameValuePair androidId = new BasicNameValuePair("deviceId",
            // Utils.getAndroidID(context));
            deviceInfoParams.add(androidId);

            BasicNameValuePair mobileModel = new BasicNameValuePair(
                    "mobileModel", Utils.getMobileModel());
            deviceInfoParams.add(mobileModel);

            BasicNameValuePair mobileManufacturer = new BasicNameValuePair(
                    "mobileManufacturer", Utils.getMobileManufacturer());
            deviceInfoParams.add(mobileManufacturer);

            BasicNameValuePair mobileId = new BasicNameValuePair("mobileId",
                    Utils.getMobileId());
            deviceInfoParams.add(mobileId);

            BasicNameValuePair mobileProduct = new BasicNameValuePair(
                    "mobileProduct", Utils.getMobileProduct());
            deviceInfoParams.add(mobileProduct);

            BasicNameValuePair applicationName = new BasicNameValuePair(
                    "applicationName", Utils.getApplicationName(context));
            deviceInfoParams.add(applicationName);

            BasicNameValuePair applicationVersionName = new BasicNameValuePair(
                    "applicationVersionName",
                    Utils.getApplicationVersionName(context));
            deviceInfoParams.add(applicationVersionName);

            BasicNameValuePair applicationVersionCode = new BasicNameValuePair(
                    "applicationVersionCode",
                    Utils.getApplicationVersionCode(context) + "");
            deviceInfoParams.add(applicationVersionCode);

            // BasicNameValuePair applicationState = new BasicNameValuePair(
            // "applicationState", Utils.getAppState(context).toString());
            // deviceInfoParams.add(applicationState);

            BasicNameValuePair screenWidth = new BasicNameValuePair(
                    "screenWidth", Utils.getDisplayWidth(context) + "");
            deviceInfoParams.add(screenWidth);

            BasicNameValuePair screenHeight = new BasicNameValuePair(
                    "screenHeight", Utils.getDisplayWidth(context) + "");
            deviceInfoParams.add(screenHeight);

            BasicNameValuePair screenDensity = new BasicNameValuePair(
                    "screenDensity", Utils.getDisplayDensity(context) + "");
            deviceInfoParams.add(screenDensity);

            BasicNameValuePair screenDensityName = new BasicNameValuePair(
                    "screenDensityName", Utils.getDisplaySize(context)
                    .toString());
            deviceInfoParams.add(screenDensityName);

            BasicNameValuePair atdPackages = new BasicNameValuePair(
                    "atdPackages", Utils.getKarinaPackages(context));
            deviceInfoParams.add(atdPackages);
        }
        return new ArrayList<BasicNameValuePair>(deviceInfoParams);
    }

    /**
     * Get Display density
     *
     * @param context Context of application
     * @return
     */
    public static int getDisplayDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.densityDpi;
    }

    /**
     * Get display size
     *
     * @param context Context of application
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
     * @param context Context of application
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
     * Making empty all data of application cache
     *
     * @param context Context of application
     * @return
     */
    public static Boolean emptyAllData(Context context) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
     * @param context Context of application
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
     * @param context Context of application
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
     * @param context Context of application
     * @return
     */
    public static boolean checkReadSMSPermission(Context context) {
        String permission = "android.permission.READ_SMS";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Getting date string in a specific format
     *
     * @param context  Context of application
     * @param cal      Calendar object
     * @param language Language code -> FA or EN and etc.
     * @return
     */
    public static String getDateString(Context context, Calendar cal,
                                       String language) {
        if (language.toLowerCase().trim().equals("fa")) {
            CalendarTool calTool = new CalendarTool(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
            return String.format("%s/%s/%s", calTool.getIranianYear(),
                    calTool.getIranianMonth(), calTool.getIranianDay());
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            return df.format(cal.getTime());
        }
    }

    /**
     * Get Long date value
     *
     * @param context  Context of application
     * @param year     Year integer
     * @param month    Month integer
     * @param day      day integer
     * @param language Language code -> FA or EN and etc.
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getDateLong(Context context, int year, int month,
                                   int day, String language) {
        if (language.toLowerCase().equals("fa")) {
            // JalaliCalendar cal = new JalaliCalendar(year, month, day +
            // 1);
            CalendarTool calTool = new CalendarTool();
            calTool.setIranianDate(year, month, day);
            return Date.parse(calTool.getGregorianDate());// cal.getTimeInMillis();//
        } else {
            return Date.parse(String.format("%s//%d//%d", year, month, day));
        }
    }

    /**
     * Getting long date value
     *
     * @param context  Context of application
     * @param date     Date object
     * @param language Language code -> FA or EN and etc.
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getDateLong(Context context, String date, String language) {
        if (language.toLowerCase().equals("fa")) {
            int year = Integer.parseInt(date.trim().split("/")[0]);
            int month = Integer.parseInt(date.trim().split("/")[1]);
            int day = Integer.parseInt(date.trim().split("/")[2]);
            // JalaliCalendar cal = new JalaliCalendar(year, month, day +
            // 1);
            CalendarTool calTool = new CalendarTool();
            calTool.setIranianDate(year, month, day);
            return Date.parse(calTool.getGregorianDate());// cal.getTimeInMillis();//
        } else {
            return Date.parse(date);
        }
    }

    /**
     * Checking a string that is numeric or not
     *
     * @param str Input string
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
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

    public static void runApplication(final Context ctx, String packageName) {
        try {
            Intent LaunchIntent = ctx.getPackageManager().getLaunchIntentForPackage(packageName);
            ctx.startActivity(LaunchIntent);
        } catch (Exception e) {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx, Farsi.Convert("برنامه ی مورد نظر یافت نشد"), Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    public static Boolean isDownloaded(Context context, String packageName, String versionCode) {
        final String appName = packageName + "." + versionCode + ".apk";
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + DIRECTORY_PUBLIC_DOWNLOAD);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().equalsIgnoreCase(appName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void installPackage(Context context, String packageName, String versionCode) {
        String appName = packageName + "." + versionCode + ".apk";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + DIRECTORY_PUBLIC_DOWNLOAD + "/" + appName)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
}