package com.wentaodemos.flashsale.db.mappers;

import com.wentaodemos.flashsale.entity.FlashSaleOrder;

public interface FlashSaleOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FlashSaleOrder record);

    int insertSelective(FlashSaleOrder record);

    FlashSaleOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FlashSaleOrder record);

    int updateByPrimaryKey(FlashSaleOrder record);

    FlashSaleOrder selectByOrderNo(String orderNo);
}