package com.ice.restring;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A StringRepository which saves/loads the strings in Shared Preferences.
 * it also keeps the strings in memory by using MemoryStringRepository internally for faster access.
 * <p>
 * it's not ThreadSafe.
 */
class SharedPrefStringRepository extends ResourceRepository implements StringRepository {
    private static final String SHARED_PREF_NAME = "Restrings";

    private SharedPreferences sharedPreferences;
    private StringRepository memoryStringRepository = new MemoryStringRepository();

    SharedPrefStringRepository(Context context) {
        super(context);
    }

    @Override
    public void setStrings(String language, Map<String, String> strings) {
        memoryStringRepository.setStrings(language, strings);
        saveStrings(language, strings);
    }

    @Override
    public void setString(String language, String key, String value) {
        memoryStringRepository.setString(language, key, value);

        Map<String, String> keyValues = memoryStringRepository.getStrings(language);
        keyValues.put(key, value);
        saveStrings(language, keyValues);
    }

    @Override
    public String getString(String language, String key) {
        return memoryStringRepository.getString(language, key);
    }

    @Override
    public Map<String, String> getStrings(String language) {
        return memoryStringRepository.getStrings(language);
    }

    @Override
    void initSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    @Override
    void loadResources() {
        Map<String, ?> strings = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : strings.entrySet()) {
            if (!(entry.getValue() instanceof String)) {
                continue;
            }

            String value = (String) entry.getValue();
            Map<String, String> keyValues = deserializeKeyValues(value);
            String language = entry.getKey();
            memoryStringRepository.setStrings(language, keyValues);
        }
    }

    private void saveStrings(String language, Map<String, String> strings) {
        String content = serializeKeyValues(strings);
        sharedPreferences.edit()
                .putString(language, content)
                .apply();
    }

    @Override
    Map<String, String> deserializeKeyValues(String content) {
        Map<String, String> keyValues = new LinkedHashMap<>();
        String[] items = content.split(",");
        for (String item : items) {
            String[] itemKeyValue = item.split("=");
            keyValues.put(itemKeyValue[0], itemKeyValue[1].replaceAll(",,", ","));
        }
        return keyValues;
    }

    @Override
    String serializeKeyValues(Map<String, String> keyValues) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> item : keyValues.entrySet()) {
            content.append(item.getKey())
                    .append("=")
                    .append(item.getValue().replaceAll(",", ",,"))
                    .append(",");
        }
        content.deleteCharAt(content.length() - 1);
        return content.toString();
    }
}
