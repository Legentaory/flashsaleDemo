package com.wentaodemos.flashsale.db.dao;

import com.wentaodemos.flashsale.entity.FlashSaleOrder;

public interface FlashSaleOderDao {

    void insertOrder(FlashSaleOrder order);

    FlashSaleOrder query(String OrderNo);

    void updateOder(FlashSaleOrder order);
}
