package com.jacob.www.a2dolist.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceManager {
    static SharedPreferences sp;

    public static void init(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private static final String KEY_CACHED_USERNAME = "2dolist_cached_username";

    public static void setCachedUsername(String username) {
        if (null != sp) {
            sp.edit().putString(KEY_CACHED_USERNAME, username).apply();
        }
    }

    public static String getCachedUsername() {
        if (null != sp) {
            return sp.getString(KEY_CACHED_USERNAME, null);
        }
        return null;
    }
    private static final String KEY_CACHED_AVATAR_PATH = "2dolist_cached_avatar_path";

    public static void setCachedAvatarPath(String path) {
        if (null != sp) {
            sp.edit().putString(KEY_CACHED_AVATAR_PATH, path).apply();
        }
    }

    public static String getCachedAvatarPath() {
        if (null != sp) {
            return sp.getString(KEY_CACHED_AVATAR_PATH, null);
        }
        return null;
    }
    private static final String KEY_REGISTER_AVATAR_PATH = "2dolist_register_avatar_path";


}
