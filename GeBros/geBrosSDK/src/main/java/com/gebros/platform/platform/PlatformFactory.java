package com.gebros.platform.platform;

import com.gebros.platform.log.GBLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by nairs on 2016-05-04.
 */
public class PlatformFactory {

    static final String CHINA_360_PLATFORM_PATH = "com.joycity.platform.plugin.china360.China360PlatformClient";
    static final String BAIDU_PLATFORM_PATH =     "com.joycity.platform.plugin.baidu.BaiduPlatformClient";
    static final String XIAOMI_PLATFORM_PATH =    "com.joycity.platform.plugin.xiaomi.XiaomiPlatformClient";
    static final String UC_PLATFORM_PATH =        "com.joycity.platform.plugin.uc.UCPlatformClient";
    static final String WANDOUJIA_PLATFORM_PATH = "com.joycity.platform.plugin.wandoujia.WandoujiaPlatformClient";
    static final String HUAWEI_PLATFORM_PATH =    "com.joycity.platform.plugin.huawei.HuaweiPlatformClient";

    public static IPlatformClient create(Platform platform) {
        Constructor constructor = null;

        try {
            switch (platform.getPlatformType()) {
                case CHINA360:
                    constructor = Class.forName(CHINA_360_PLATFORM_PATH).getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                case BAIDU:
                    constructor = Class.forName(BAIDU_PLATFORM_PATH).getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                case XIAOMI:
                    constructor = Class.forName(XIAOMI_PLATFORM_PATH).getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                case UC:
                    constructor = Class.forName(UC_PLATFORM_PATH).getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                case WANDOUJIA:
                    constructor = Class.forName(WANDOUJIA_PLATFORM_PATH).getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                case HUAWEI:
                    constructor = Class.forName(HUAWEI_PLATFORM_PATH).getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                case DEFAULT:
                    constructor = Class.forName("com.gebros.platform.platform.DefaultPlatformClient").getDeclaredConstructor(Platform.class);
                    constructor.setAccessible(true);
                    return (IPlatformClient)constructor.newInstance(platform);

                default:
                    return null;
            }
        } catch (ClassNotFoundException e) {
            GBLog.e(e, "Exception = %s", e.getMessage());
        } catch (IllegalAccessException e) {
            GBLog.e(e, "Exception = %s", e.getMessage());
        } catch (InstantiationException e) {
            GBLog.e(e, "Exception = %s", e.getMessage());
        } catch (NoSuchMethodException e) {
            GBLog.e(e, "Exception = %s", e.getMessage());
        } catch (InvocationTargetException e) {
            GBLog.e(e, "Exception = %s", e.getMessage());
        }

        return null;
    }
}


