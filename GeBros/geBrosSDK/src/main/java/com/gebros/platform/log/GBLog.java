package com.gebros.platform.log;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by nairs77@joycity.com on 5/2/16.
 *
 * 1: Log.v - debugging color black , and any messages will be output, where v represents the verbose verbose mean, usually is Log.v ("", "");
 * 2: Log.d - the output color is blue , the only output debug debugging meaning, but he would output the upper filter up through of DDMS Logcat label to select.
 * 3: Log.i - output color is green , general tips, news information, it does not the output Log.v Log.d information, but will display the information of i, w and e
 * 4: Log.w - mean orange , can be seen as a warning The warning, in general we need to optimize the Android code, and will output it after Log.e.
 * 5: Log.e - is red , you can think of error error here only to show the red error message, these errors we need careful analysis.
 *
 * * <a href="http://developer.android.com/intl/ko/tools/debugging/debugging-log.html"> Debug URL </a>
 */




public class GBLog {

    public enum Mode {
        DEBUG,
        RELEASE
    };

    enum LogLevel {
        VERBOSE,
        WARN,
        INFO,
        ERROR
    };

    private static String TAG = "[GBLog]";
    private static volatile boolean DISABLED = false;

    public static void setTag(String tagName) {
        TAG = tagName;
    }

    public static void enableLog() {
        DISABLED = false;
    }

    public static void disableLog() {
        DISABLED = true;
    }

    public static void v(String format, Object... args) {
        if(DISABLED)
            return;
        Log.v(TAG, String.format(format, args));
    }

    public static void d(String format, Object... args) {
        if(DISABLED)
            return;
        Log.d(TAG, String.format(format, args));
    }

    public static void i(String format, Object... args) {
        Log.i(TAG, String.format(format, args));
    }

    public static void e(Throwable tr, String format, Object... args) {
        Log.e(TAG, String.format(format, args), tr);
    }

//    static final String SERVER_LOG_URI = JoypleConfig.getContentsServer() + "/logs/client/add";

    protected static LogLevel logLevel = LogLevel.VERBOSE;
    protected static Mode logMode = Mode.RELEASE;

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    public static boolean isTestMode() {
//        return (logLevel.ordinal() <= Joyple.LogLevel.TEST.ordinal());
//    }

    public static boolean isDebug() {
        return (logMode == Mode.DEBUG) ? true : false;
    }

    public static boolean isRelease() {
        return (logMode == Mode.RELEASE) ? true : false;
    }

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(Mode mode) {
        logMode = mode;
    }

}
