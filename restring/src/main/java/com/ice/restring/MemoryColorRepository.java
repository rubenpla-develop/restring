package com.ice.restring;

import java.util.LinkedHashMap;
import java.util.Map;

class MemoryColorRepository implements ColorRepository {

    private Map<String, String> colors = new LinkedHashMap<>();

    @Override
    public void setColors(Map<String, String> colorResources) {
        colors = colorResources;
    }

    @Override
    public void setColor(String key, String value) {
        if (!colors.containsKey(key)) {
            colors.put(key, value);
        }
    }

    @Override
    public String getColor(String key) {
        if (colors.containsKey(key)) {
            return colors.get(key);
        }

        return null;
    }

    @Override
    public Map<String, String> getColors() {
         return new LinkedHashMap<>(colors);
    }
}
