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

    private static final String KEY_CACHED_NICKNAME = "2dolist_cached_nickname";

    public static void setCachedNickname(String nickname) {
        if (null != sp) {
            sp.edit().putString(KEY_CACHED_NICKNAME, nickname).apply();
        }
    }

    public static String getCachedNickname() {
        if (null != sp) {
            return sp.getString(KEY_CACHED_NICKNAME, null);
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
    private static final String TODO_TEXT = "todo_text";
    public static void setTodoText(String path) {
        if (null != sp) {
            sp.edit().putString(TODO_TEXT, path).apply();
        }
    }

    public static String getTodoText() {
        if (null != sp) {
            return sp.getString(TODO_TEXT, null);
        }
        return null;
    }
    private static final String TODO_TEXT_1 = "todo_text1";
    public static void setTodoText1(String path) {
        if (null != sp) {
            sp.edit().putString(TODO_TEXT_1, path).apply();
        }
    }

    public static String getTodoText1() {
        if (null != sp) {
            return sp.getString(TODO_TEXT_1, null);
        }
        return null;
    }
    private static final String GROUP_TASK = "group_task";
    public static void setGroupTask(String task) {
        if (null != sp) {
            sp.edit().putString(GROUP_TASK, task).apply();
        }
    }

    public static String getGroupTask() {
        if (null != sp) {
            return sp.getString(GROUP_TASK, null);
        }
        return null;
    }
}
