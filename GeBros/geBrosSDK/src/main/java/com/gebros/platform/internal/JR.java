package com.gebros.platform.internal;

import android.content.res.Resources;

import com.gebros.platform.GBSdk;
import com.gebros.platform.util.GBValidator;

/**
 * Created by nairs77@joycity.com on 5/13/16.
 */


public class JR {

    private static final String TAG = "Joycity ResourceManager";
    private static String packageName;
    private static Resources res;

    private static final Resources getResources() {

        GBValidator.notNull(GBSdk.getApplicationContext(), "Joyple has been not initialized.");

        if (res == null)
            res = GBSdk.getApplicationContext().getResources();

        return res;
    }

    private static final String packageName() {

        if (packageName == null)
            packageName = GBSdk.getApplicationContext().getPackageName();

        return packageName;
    }

    private static final int pick(String name, String type) {
        return getResources().getIdentifier(name, type, packageName());
    }

    public static final int string(String name) {
        return pick(name, "string");
    }

    public static final int dimen(String name) {
        return pick(name, "dimen");
    }

    public static final int drawable(String name) {
        return pick(name, "drawable");
    }

    public static final int id(String name) {
        return pick(name, "id");
    }

    public static final int attr(String name) {
        return pick(name, "attr");
    }

    public static final int layout(String name) {
        return pick(name, "layout");
    }

    public static final int menu(String name) {
        return pick(name, "menu");
    }

    public static final int style(String name) {
        return pick(name, "style");
    }

    public static int integer(String name) {
        return pick(name, "integer");
    }

    public static int anim(String name) {
        return pick(name, "anim");
    }

    public static int raw(String name) {
        return pick(name, "raw");
    }

    public static int color(String name) {
        return pick(name, "color");
    }

    public static int array(String name) {
        return pick(name, "array");
    }

    public static int bool(String name) {
        return pick(name, "bool");
    }
}



