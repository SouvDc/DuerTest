package com.dc.duertest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by DengJia on 2018/7/20.
 */

public class SharedPreferenceUtil {
    public static final String KEY_CAMERA_FEATURES = "key_camera_features";
    public static final String KEY_CAMERA_ANGLES = "key_camera_angles";

    public static final String FILE_NAME = "robot_info_data";

    /**
     * @param context
     * @param key
     * @param object
     */
    public static boolean put(Context context, String key, Object object) {
        if (context == null) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();

        if (object == null) {
            object = "";
        }

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

       return editor.commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, @NonNull Object defaultObject) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_MULTI_PROCESS);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
//        else {
//            throw new IllegalArgumentException("defaultObject Unknown type !!!");
//        }
        return null;

    }

}
