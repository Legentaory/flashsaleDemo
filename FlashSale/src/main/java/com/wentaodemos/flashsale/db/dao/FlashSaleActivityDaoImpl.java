package com.wentaodemos.flashsale.db.dao;

import com.wentaodemos.flashsale.db.mappers.FlashSaleActivityMapper;
import com.wentaodemos.flashsale.entity.FlashSaleActivity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Repository
public class FlashSaleActivityDaoImpl implements FlashSaleActivityDao {

    private Logger logger;
    @Resource
    private FlashSaleActivityMapper mapper;

    @Override
    public List<FlashSaleActivity> queryFlashSaleActivitiesByStatus(int activityStatus) {
        return mapper.selectByStatus(activityStatus);
    }

    @Override
    public void insertFlashSaleActivity(FlashSaleActivity activity) {
        mapper.insert(activity);
    }

    @Override
    public FlashSaleActivity queryFlashSaleActivityById(long activityId) {
        return mapper.selectByPrimaryKey(activityId);
    }

    @Override
    public void updateFlashSaleActivity(FlashSaleActivity activity) {
        mapper.updateByPrimaryKey(activity);
    }

    @Override
    public boolean lockStock(Long flashsaleActivityId) {
        int result = mapper.lockStock(flashsaleActivityId);
        if (result < 1){
            logger.error("Lock stock fail");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductStock(Long flashsaleActivityId) {
        int result = mapper.deductStock(flashsaleActivityId);
        if(result < 1) {
            logger.error("Failed to deduct");
            return false;
        }
        return true;
    }

    @Override
    public void revertStock(Long flashsaleActivityId) {
        mapper.revertStock(flashsaleActivityId);
    }


}
