package com.gebros.platform;

import java.util.HashSet;

/**
 * Created by nairs77 on 2017. 1. 18..
 */

public class GBAccountStore {

    private static final String TAG = GBAccountStore.class.getCanonicalName();

    private HashSet<IAuthAccount> accounts;

    private static final class GBAccountStoreHolder {
        public static GBAccountStore instance = new GBAccountStore();
    }

    public static GBAccountStore getInstance() {
        return GBAccountStoreHolder.instance;
    }


}
