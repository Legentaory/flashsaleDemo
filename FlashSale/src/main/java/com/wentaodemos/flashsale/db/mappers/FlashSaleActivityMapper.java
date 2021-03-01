package com.wentaodemos.flashsale.db.mappers;

import com.wentaodemos.flashsale.entity.FlashSaleActivity;

import java.util.List;

public interface FlashSaleActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FlashSaleActivity record);

    int insertSelective(FlashSaleActivity record);

    FlashSaleActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FlashSaleActivity record);

    int updateByPrimaryKey(FlashSaleActivity record);

    List<FlashSaleActivity> selectByStatus(int activityStatus);

    int lockStock(Long flashsaleActivityId);

    int deductStock(Long flashsaleActivityId);

    void revertStock(Long flashsaleActivityId);
}