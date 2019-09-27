package com.ice.restring;

/**
 * Contains configuration properties for initializing Restring.
 */
public class RestringConfig {

    private boolean persist;
    private Restring.StringsLoader stringsLoader;
    private Restring.ColorLoader colorLoader;

    public boolean isPersist() {
        return persist;
    }

    public Restring.StringsLoader getStringsLoader() {
        return stringsLoader;
    }

    public Restring.ColorLoader getColorLoader() {
        return colorLoader;
    }

    private RestringConfig() {
    }

    public static class Builder {
        private boolean persist;
        private Restring.StringsLoader stringsLoader;
        private Restring.ColorLoader colorLoader;

        public Builder persist(boolean persist) {
            this.persist = persist;
            return this;
        }

        public Builder stringsLoader(Restring.StringsLoader loader) {
            this.stringsLoader = loader;
            return this;
        }

        public Builder colorLoader(Restring.ColorLoader loader) {
            this.colorLoader = loader;
            return this;
        }

        public RestringConfig build() {
            RestringConfig config = new RestringConfig();
            config.persist = persist;
            config.stringsLoader = stringsLoader;
            config.colorLoader = colorLoader;
            return config;
        }
    }

    static RestringConfig getDefault() {
        return new Builder()
                .persist(true)
                .build();
    }
}