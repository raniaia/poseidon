package org.laniakea.poseidon.framework.cache;

import org.laniakea.poseidon.framework.config.Config;
import org.laniakea.poseidon.framework.timer.Timer;

/**
 * Create by 2BKeyboard on 2019/12/7 0:47
 */
public class CacheRefreshTimer implements Timer {

    private PoseidonCache cache;

    public CacheRefreshTimer(PoseidonCache cache){
        this.cache = cache;
    }

    @Override
    public void run() {
        cache.refreshAll();
    }

    @Override
    public long time() {
        return Config.getInstance().getRefresh();
    }
}