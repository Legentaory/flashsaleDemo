package com.wentaodemos.flashsale.db.dao;

import com.wentaodemos.flashsale.db.mappers.FlashSaleCommodityMapper;
import com.wentaodemos.flashsale.entity.FlashSaleCommodity;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FlashSaleCommodityDaoImpl implements FlashSaleCommodityDao{

    @Resource
    private FlashSaleCommodityMapper mapper;


    @Override
    public FlashSaleCommodity queryFlashSaleCommodityById(long commodityId) {
        return mapper.selectByPrimaryKey(commodityId);
    }
}
