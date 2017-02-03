package com.gebros.platform;

import java.util.Properties;

/**
 * Created by gebros.nairs77@gmail.com on 5/6/16.
 */
final class GBConfigProxy {

    private static volatile GBConfigProxy instance;
    private Properties configProperty;
    private GBConfig currentConfig;

    public static final String CONFIG_PROPERTY_FILE = "platform.properties";
    public static final String PLATFORM_TYPE = "joycity.platform.type";
    public static final String PLATFORM_TYPE_APPKEY="joycity.platform.type.appkey";

    GBConfigProxy() {
        configProperty = new Properties();
    }

    static GBConfigProxy getInstance() {

        if (instance == null) {
            synchronized (GBConfigProxy.class) {
                if (instance == null) {
                    instance = new GBConfigProxy();
                }
            }
        }

        return instance;
    }
/*
    void loadConfigData() {
        Context context = GB.getApplicationContext();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream inputStream = assetManager.open(CONFIG_PROPERTY_FILE);
            configProperty.load(inputStream);
        } catch (IOException e) {
            // TODO : Show Error Message
        }

        //GBConfig config = new GBConfig(configProperty);
        GBConfig.Builder builder = new GBConfig.Builder()
                .addConfig(PLATFORM_TYPE, getPlatformName());

        currentConfig = builder.build();
    }
*/
    GBConfig getCurrentConfig() {
        return currentConfig;
    }

    String getPlatformName() {
        return configProperty.getProperty(PLATFORM_TYPE);
    }
}
