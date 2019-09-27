package com.ice.restring;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashMap;
import java.util.Map;

public class SharedPrefColorRepository extends ResourceRepository implements ColorRepository {

    private static final String SHARED_PREF_NAME = "ResColors";
    private static final String CONTENT_KEY = "Colors";

    private SharedPreferences sharedPreferences;

    private MemoryColorRepository memoryColorRepository = new MemoryColorRepository();

    SharedPrefColorRepository(Context context) {
        super(context);
    }

    @Override
    void initSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    @Override
    void loadResources() {
        Map<String, ?> colors = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : colors.entrySet()) {
            if (!(entry.getValue() instanceof  String)) {
                continue;
            }

            String value = (String) entry.getValue();
            Map<String, String> keyValues = deserializeKeyValues(value);
            memoryColorRepository.setColors(keyValues);
        }
    }

    private void saveColors(Map<String, String> colors) {
        String content = serializeKeyValues(colors);
        sharedPreferences.edit()
                .putString(CONTENT_KEY, content)
                .apply();
    }

    @Override
    Map<String, String> deserializeKeyValues(String content) {
        Map<String, String> keyValues = new LinkedHashMap<>();
        String[] items = content.split(",");

        for (String item: items) {
            String[] itemKeyValue = item.split("=");
            keyValues.put(itemKeyValue[0], itemKeyValue[1].replaceAll(",,", ","));
        }

        return keyValues;
    }

    @Override
    String serializeKeyValues(Map<String, String> keyValues) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> items : keyValues.entrySet()) {
            content.append(items.getKey())
                    .append("=")
                    .append(items.getValue().replaceAll(",",",,"))
                    .append(",");
        }

        content.deleteCharAt(content.length() -1);
        return content.toString();
    }

    @Override
    public void setColors(Map<String, String> colors) {
        memoryColorRepository.setColors(colors);
        saveColors(colors);
    }

    @Override
    public void setColor(String key, String value) {
            memoryColorRepository.setColor(key, value);

            Map<String, String> keyValues = memoryColorRepository.getColors();
            keyValues.put(key, value);
            saveColors(keyValues);
    }

    @Override
    public String getColor(String key) {
        return memoryColorRepository.getColor(key);
    }

    @Override
    public Map<String, String> getColors() {
        return memoryColorRepository.getColors();
    }
}
