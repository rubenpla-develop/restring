package com.ice.restring;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

abstract class ResourceRepository {

    ResourceRepository(Context context) {
        initSharedPreferences(context);
        loadResources();
    }

    abstract void initSharedPreferences(Context context);
    abstract void loadResources();
    abstract Map<String, String> deserializeKeyValues(String content);
    abstract String serializeKeyValues(Map<String, String> keyValues);
}
