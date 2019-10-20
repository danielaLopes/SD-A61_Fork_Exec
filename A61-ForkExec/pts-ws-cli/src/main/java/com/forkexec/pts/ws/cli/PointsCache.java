package com.forkexec.pts.ws.cli;

import com.forkexec.pts.ws.BalanceInfo;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class PointsCache {

    private final int size = 5;
    private Map<String, BalanceInfo> cacheMap = new HashMap<>();

    public PointsCache() {}

    public void put(String userEmail, BalanceInfo balanceInfo) {
        if(cacheMap.size() >= this.size) {
            int random = (int) Math.random() * cacheMap.size();
            cacheMap.remove(Array.get(cacheMap.keySet().toArray(), random));
        }
        System.out.println("Updating value in cache: " + balanceInfo.getBalance());
        cacheMap.put(userEmail, balanceInfo);
    }

    public BalanceInfo find(String userEmail) {
        return cacheMap.get(userEmail);
    }
}
