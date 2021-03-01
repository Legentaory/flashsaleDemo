package com.wentaodemos.flashsale.db.dao;

import com.wentaodemos.flashsale.entity.FlashSaleActivity;

import java.util.*;

public interface FlashSaleActivityDao {

    public List<FlashSaleActivity> queryFlashSaleActivitiesByStatus(int activityStatus);

    public void insertFlashSaleActivity(FlashSaleActivity activity);

    public FlashSaleActivity queryFlashSaleActivityById(long activityId);

    public void updateFlashSaleActivity(FlashSaleActivity activity);

    boolean lockStock(Long flashsaleActivityId);

    boolean deductStock(Long flashsaleActivityId);

    void revertStock(Long flashsaleActivityId);
}
