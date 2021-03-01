package com.wentaodemos.flashsale.db.dao;

import com.wentaodemos.flashsale.db.mappers.FlashSaleOrderMapper;
import com.wentaodemos.flashsale.entity.FlashSaleOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FlashSaleOrderImpl implements FlashSaleOderDao{

    @Resource
    private FlashSaleOrderMapper mapper;

    @Override
    public void insertOrder(FlashSaleOrder order) {
        mapper.insert(order);
    }

    @Override
    public FlashSaleOrder query(String orderNo) {
        return mapper.selectByOrderNo(orderNo);
    }

    @Override
    public void updateOder(FlashSaleOrder order) {
        mapper.updateByPrimaryKey(order);
    }
}
