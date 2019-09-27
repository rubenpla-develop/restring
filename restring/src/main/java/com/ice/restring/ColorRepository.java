package com.ice.restring;

import java.util.Map;

public interface ColorRepository {

    void setColors(Map<String, String> colors);

    void setColor(String key, String value);

    String getColor(String key);

    Map<String, String> getColors();
}
